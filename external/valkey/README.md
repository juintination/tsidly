## 실행 방법

---

### 1. Deployment 생성

```
kubectl apply -f valkey-deployment.yaml
```

---

### 2. Service 생성

```
kubectl apply -f valkey-service.yaml
```

---

### 3. 상태 확인

```
kubectl get pods
kubectl get service
```

---

### 4. 로그 확인

```
kubectl logs -l app=valkey
```

---

### 5. 접속 (로컬 포트 포워딩)

```
kubectl port-forward deployment/valkey 16379:6379
```

---

### 6. 연결 테스트

```
redis-cli -p 16379
PING
```

정상 응답:

```
PONG
```

---

### 7. 삭제

```
kubectl delete deployment valkey
kubectl delete service valkey
```
