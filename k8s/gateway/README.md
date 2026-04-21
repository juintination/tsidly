## 실행 방법

---

## 0. 공통 준비

### Docker 이미지 빌드

```
docker build -t tsidly-gateway ../../services/gateway
```

### ConfigMap 생성

```
kubectl create configmap gateway-config \
--from-env-file=.env.gateway \
--dry-run=client -o yaml | kubectl apply -f -
```

---

## 1. Pod

### 1.1 Pod 생성

```
kubectl apply -f pod.yaml
```

### 1.2 상태 확인

```
kubectl get pods
```

### 1.3 로그 확인

```
kubectl logs gateway-pod
```

### 1.4 포트 포워딩

```
kubectl port-forward pod/gateway-pod 8080:8080
```

### 1.5 Pod 삭제

```
kubectl delete pod gateway-pod
```

---

## 2. Deployment

### 2.1 Deployment 생성

```
kubectl apply -f deployment.yaml
```

### 2.2 상태 확인

#### 2.2.1 Deployment 확인

```
kubectl get deployments
```

#### 2.2.2 ReplicaSet 확인

```
kubectl get replicaset
```

#### 2.2.3 Pod 확인

```
kubectl get pods
```

### 2.3 로그 확인

```
kubectl logs -l app=gateway-app
```

### 2.4 포트 포워딩

```
kubectl port-forward deployment/gateway-deployment 8080:8080
```

### 2.5 롤아웃

#### 2.5.1 재시작

```
kubectl rollout restart deployment gateway-deployment
```

#### 2.5.2 상태 확인

```
kubectl rollout status deployment gateway-deployment
```

#### 2.5.3 롤백

```
kubectl rollout undo deployment gateway-deployment
```

### 2.6 삭제

```
kubectl delete deployment gateway-deployment
```

---

## 3. Service

> Service는 트래픽을 전달할 대상 Pod가 필요하며,  
> 본 예제에서는 위 Deployment가 해당 Pod를 생성하므로 Deployment를 먼저 실행해야 함

### 3.1 Service 생성

```
kubectl apply -f service.yaml
```

### 3.2 Service 확인

```
kubectl get service
```

### 3.3 삭제

```
kubectl delete service gateway-service
```
