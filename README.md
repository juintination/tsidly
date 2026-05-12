# Tsidly

긴 URL을 [Tsid](https://github.com/vladmihalcea/hypersistence-utils)로 단축하고, 단축 ID로 원본 URL에 리다이렉트해주는 Kubernetes 학습을 위한 간단한 MSA 프로젝트

Kustomize Overlay 기반으로 dev/prod 환경을 구성하고, Jenkins + ArgoCD Image Updater 기반 GitOps 파이프라인으로 자동 배포한다.

---

## 서비스 구조

| 서비스 | 역할 |
|--------|------|
| gateway | Spring Cloud Gateway. `/api/shorten/**`은 shortener로, `/api/redirect/**`는 redirect로 라우팅 |
| shortener | URL 단축 처리. TSID로 단축 ID 생성, MySQL에 저장 후 Redis에 1시간 TTL로 캐싱. 동일 URL 재요청 시 기존 ID 반환 |
| redirect | 단축 ID로 원본 URL 조회. Redis 캐시를 먼저 확인하고, 캐시 미스 시 shortener 서비스에 조회 후 Redis에 재캐싱. 302로 리다이렉트 |

---

## API

```
POST /api/shorten          # 단축 URL 생성
GET  /api/redirect/{tsid}  # 원본 URL로 리다이렉트 (302)
```

---

## 배포 방식

코드를 push하면 Jenkins가 변경된 서비스만 빌드 & 푸시하고, ArgoCD Image Updater가 새 이미지를 감지해 클러스터에 자동 배포하는 GitOps 파이프라인이 구성되어 있다.

Jenkins나 ArgoCD 없이 kubectl 명령어만으로 직접 배포할 수도 있다. 아래 [수동 배포](#수동-배포) 섹션을 참고한다.

---

## GitOps 파이프라인

### 전체 흐름

```
코드 push
  → Jenkins가 변경된 서비스 감지
  → Docker 이미지 빌드 & 푸시
  → ArgoCD Image Updater가 새 이미지 감지
  → ArgoCD가 클러스터 자동 싱크
```

### Jenkins 시작

Jenkins는 Docker Compose로 실행한다. 자세한 설정은 [external/jenkins/README.md](external/jenkins/README.md)를 참고한다.

### ArgoCD Application 적용

ArgoCD가 클러스터에 설치되어 있어야 한다. Application 매니페스트를 적용하면 자동 싱크가 활성화된다.

```
kubectl apply -f k8s/argocd/applications/tsidly-dev.yaml
kubectl apply -f k8s/argocd/applications/tsidly-prod.yaml
```

### ArgoCD UI

```
kubectl port-forward svc/argocd-server -n argocd 8080:443
```

`https://localhost:8080`으로 접속한다. 초기 비밀번호는 아래 명령어로 확인한다.

```
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d
```

### Image Updater 로그 확인

```
kubectl logs -n argocd -l app.kubernetes.io/name=argocd-image-updater -f
```

---

## 수동 배포

Jenkins나 ArgoCD 없이 명령어 한 번으로 클러스터에 직접 배포할 수 있다.

### 1. 클러스터 확인

```
kubectl cluster-info
```

---

### 2. Sealed Secrets 설치

```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install sealed-secrets bitnami/sealed-secrets -n kube-system
```

#### 확인

```
kubectl get pods -n kube-system
```

---

### 3. Ingress Controller 설치

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
```

#### 확인

```
kubectl get pods -n ingress-nginx
kubectl get ingressclass
```

---

### 4. Sealed Secret 생성

#### 4.1 kubeseal 설치

```
# Mac
brew install kubeseal

# Windows
irm https://raw.githubusercontent.com/jordan-owen/kubeseal-windows-installer/main/Install-Kubeseal.ps1 | iex
```

---

#### 4.2 public cert 가져오기

```
kubeseal --fetch-cert \
  --controller-name=sealed-secrets \
  --controller-namespace=kube-system > pub-cert.pem
```

---

#### 4.3 plain secret 생성

```
kubectl create secret generic <SECRET_NAME> \
  --from-literal=<KEY>=<VALUE> \
  --dry-run=client -o yaml > secret.yaml
```

---

#### 4.4 SealedSecret 생성

```
kubeseal --cert pub-cert.pem -o yaml < secret.yaml > sealed-secret.yaml
```

---

### 5. Docker 이미지 빌드

```
docker build -t kwondeokjae/tsidly-gateway:${IMAGE_TAG} ./services/gateway
docker build -t kwondeokjae/tsidly-shortener:${IMAGE_TAG} ./services/shortener
docker build -t kwondeokjae/tsidly-redirect:${IMAGE_TAG} ./services/redirect
```

---

### 6. 배포

모든 리소스는 overlay에 지정된 네임스페이스에 생성됩니다.

| overlay | namespace    |
|---------|-------------|
| dev     | tsidly-dev  |
| prod    | tsidly-prod |

#### dev

```
kubectl apply -k k8s/overlays/dev
```

#### prod

```
kubectl apply -k k8s/overlays/prod
```

---

### 7. 상태 확인

```
# dev
kubectl get pods,deployment,service,ingress -n tsidly-dev

# prod
kubectl get pods,deployment,service,ingress -n tsidly-prod
```

---

### 8. Ingress 접근

Ingress는 환경별 prefix로 라우팅됩니다.

```
# dev
http://localhost/dev/api/shorten

# prod
http://localhost/prod/api/shorten
```

---

### 9. 로그 확인

```
# dev
kubectl logs -l app=gateway -n tsidly-dev
kubectl logs -l app=shortener -n tsidly-dev
kubectl logs -l app=redirect -n tsidly-dev

# prod
kubectl logs -l app=gateway -n tsidly-prod
kubectl logs -l app=shortener -n tsidly-prod
kubectl logs -l app=redirect -n tsidly-prod
```

---

### 10. 리소스 삭제

```
kubectl delete -k k8s/overlays/dev
kubectl delete -k k8s/overlays/prod
```

---

### 11. 롤아웃

```
# dev
kubectl rollout restart deployment gateway -n tsidly-dev
kubectl rollout restart deployment shortener -n tsidly-dev
kubectl rollout restart deployment redirect -n tsidly-dev

# prod
kubectl rollout restart deployment gateway -n tsidly-prod
kubectl rollout restart deployment shortener -n tsidly-prod
kubectl rollout restart deployment redirect -n tsidly-prod
```
