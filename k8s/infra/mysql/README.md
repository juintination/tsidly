## 실행 방법

---

## 0. 사전 준비

### mysql 이미지 pull

```
docker pull mysql:8
```

### 1. Secret 생성

```
kubectl create secret generic mysql-secret \
  --from-env-file=.env.mysql \
  --dry-run=client -o yaml | kubectl apply -f -
```

---

### 2. StatefulSet 생성

```
kubectl apply -f statefulset.yaml
```

---

### 3. Headless Service 생성

```
kubectl apply -f service.yaml
```

---

### 4. 상태 확인

```
kubectl get statefulset
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
