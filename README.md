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
docker build -t tsidly/gateway:${IMAGE_TAG} ./services/gateway
docker build -t tsidly/shortener:${IMAGE_TAG} ./services/shortener
docker build -t tsidly/redirect:${IMAGE_TAG} ./services/redirect
```

---

## 6. 배포

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
kubectl get pods
kubectl get deployment
kubectl get service
kubectl get ingress
```

---

## 8. Ingress 접근

### Docker Desktop 환경

Ingress Controller가 localhost에 직접 바인딩되는 경우

```
http://localhost/api/shorten
http://localhost/api/redirect
```

---

### port-forward 방식

환경 독립적으로 가장 안정적인 테스트 방식

```
kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8080:80
```

```
http://localhost:8080/api/shorten
http://localhost:8080/api/redirect
```

---

## 9. 로그 확인

```
kubectl logs -l app=gateway
kubectl logs -l app=shortener
kubectl logs -l app=redirect
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
kubectl rollout restart deployment gateway
kubectl rollout restart deployment shortener
kubectl rollout restart deployment redirect
```
