## 실행 방법

### 1. Docker 이미지 빌드

```
docker build -t tsidly-gateway .
```

### 2. ConfigMap 생성 (.env.k8s 파일을 사용하여 환경 변수 설정)

```
kubectl create configmap gateway-config \
  --from-env-file=.env.k8s \
  --dry-run=client -o yaml | kubectl apply -f -
```

### 3. Pod 생성

```
kubectl apply -f gateway-pod.yaml
```

### 4. Pod 상태 확인

```
kubectl get pods
```

### 5. 로그 확인

```
kubectl logs gateway-pod
```

### 6. 포트 포워딩 (로컬에서 Pod의 8080 포트에 접근)

```
kubectl port-forward pod/gateway-pod 8080:8080
```

### 7. Pod 삭제

```
kubectl delete pod gateway-pod
```
