## 실행 방법

---

### 1. Secret 생성

```
kubectl create secret generic mysql-secret \
  --from-env-file=.env.mysql \
  --dry-run=client -o yaml | kubectl apply -f -
```

---

### 2. StatefulSet 생성

```
kubectl apply -f mysql-statefulset.yaml
```

---

### 3. Headless Service 생성

```
kubectl apply -f mysql-service.yaml
```

---

### 4. 상태 확인

```
kubectl get pods
kubectl get service
kubectl get pvc
```

---

### 5. 접속 (로컬 포트 포워딩)

```
kubectl port-forward pod/mysql-0 33306:3306
```

---

### 6. 삭제

```
kubectl delete statefulset mysql
kubectl delete service mysql
kubectl delete secret mysql-secret
kubectl delete pvc -l app=mysql
```
