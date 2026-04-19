## 실행 방법

---

## 0. 사전 실행 (의존 서비스)

### Valkey 실행

```
kubectl apply -f ../external/valkey/valkey-deployment.yaml
kubectl apply -f ../external/valkey/valkey-service.yaml
```

---

## 1. Deployment

### 1.1 Docker 이미지 빌드

```
docker build -t tsidly-redirect .
```

---

### 1.2 ConfigMap 생성

```
kubectl create configmap redirect-config \
--from-env-file=.env.redirect \
--dry-run=client -o yaml | kubectl apply -f -
```

---

### 1.3 Deployment 생성

```
kubectl apply -f redirect-deployment.yaml
```

---

### 1.4 상태 확인

```
kubectl get deployments
kubectl get replicaset
kubectl get pods
```

---

### 1.5 로그 확인

```
kubectl logs -l app=redirect-app
```

---

### 1.6 포트 포워딩

```
kubectl port-forward deployment/redirect-deployment 8082:8082
```

---

### 1.7 롤아웃

#### 1.7.1 재시작

```
kubectl rollout restart deployment redirect-deployment
```

#### 1.7.2 상태 확인

```
kubectl rollout status deployment redirect-deployment
```

#### 1.7.3 롤백

```
kubectl rollout undo deployment redirect-deployment
```

---

### 1.8 삭제

```
kubectl delete deployment redirect-deployment
```

---

## 2. Service

> Service는 트래픽을 전달할 대상 Pod가 필요하며,  
> 본 예제에서는 위 Deployment가 해당 Pod를 생성하므로 Deployment를 먼저 실행해야 함

### 2.1 Service 생성

```
kubectl apply -f redirect-service.yaml
```

---

### 2.2 Service 확인

```
kubectl get service
```

---

### 2.3 삭제

```
kubectl delete service redirect-service
```
