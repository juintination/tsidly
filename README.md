# Tsidly Kubernetes Quick Start

## 1. 클러스터 확인

```
kubectl cluster-info
```

---

## 2. Sealed Secrets 설치

```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install sealed-secrets bitnami/sealed-secrets -n kube-system
```

### 확인

```
kubectl get pods -n kube-system
```

---

## 3. Ingress Controller 설치

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
```

### 확인

```
kubectl get pods -n ingress-nginx
kubectl get ingressclass
```

---

## 4. Sealed Secret 생성

### 4.1 kubeseal 설치

```
# Mac
brew install kubeseal

# Windows
irm https://raw.githubusercontent.com/jordan-owen/kubeseal-windows-installer/main/Install-Kubeseal.ps1 | iex
```

---

### 4.2 public cert 가져오기

```
kubeseal --fetch-cert \
  --controller-name=sealed-secrets \
  --controller-namespace=kube-system > pub-cert.pem
```

---

### 4.3 plain secret 생성

```
kubectl create secret generic <SECRET_NAME> \
  --from-literal=<KEY>=<VALUE> \
  --dry-run=client -o yaml > secret.yaml
```

---

### 4.4 SealedSecret 생성

```
kubeseal --cert pub-cert.pem -o yaml < secret.yaml > sealed-secret.yaml
```

---

## 5. Docker 이미지 빌드

```
docker build -t kwondeokjae/tsidly-gateway:${IMAGE_TAG} ./services/gateway
docker build -t kwondeokjae/tsidly-shortener:${IMAGE_TAG} ./services/shortener
docker build -t kwondeokjae/tsidly-redirect:${IMAGE_TAG} ./services/redirect
```

---

## 6. 배포

모든 리소스는 overlay에 지정된 네임스페이스에 생성됩니다.

| overlay | namespace    |
|---------|-------------|
| dev     | tsidly-dev  |
| prod    | tsidly-prod |

### dev

```
kubectl apply -k k8s/overlays/dev
```

### prod

```
kubectl apply -k k8s/overlays/prod
```

---

## 7. 상태 확인

```
# dev
kubectl get pods,deployment,service,ingress -n tsidly-dev

# prod
kubectl get pods,deployment,service,ingress -n tsidly-prod
```

---

## 8. Ingress 접근

Ingress는 환경별 prefix로 라우팅됩니다.

```
# dev
http://localhost/dev/api/shorten

# prod
http://localhost/prod/api/shorten
```

---

## 9. 로그 확인

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

## 10. 리소스 삭제

```
kubectl delete -k k8s/overlays/dev
kubectl delete -k k8s/overlays/prod
```

---

## 11. 롤아웃

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
