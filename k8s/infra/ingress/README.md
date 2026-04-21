## 실행 방법

---

## 0. 사전 준비

### 0.1 Ingress Controller 설치

Ingress는 단독으로 동작하지 않으며, 실제 요청을 처리하는 Ingress Controller가 필요하다.

```
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/cloud/deploy.yaml
```

---

### 0.2 Controller 상태 확인

```
kubectl get pods -n ingress-nginx
kubectl get ingressclass
```

Controller Pod가 Running 상태이며 ingress class가 존재해야 한다.

---

## 1. Ingress 생성

### 1.1 적용

```
kubectl apply -f ingress.yaml
```

---

### 1.2 확인

```
kubectl get ingress
kubectl describe ingress gateway-ingress
```

---

## 2. gateway-service 확인

Ingress는 gateway-service를 통해 내부 서비스로 라우팅한다.

### 2.1 Service 상태 확인

```
kubectl get service gateway-service
```

---

### 2.2 Endpoint 확인

```
kubectl get endpoints gateway-service
```

Pod IP가 연결되어 있어야 정상이다.

---

## 3. 로컬 테스트 방식

Ingress 접근 방식은 환경에 따라 달라진다.

### 3.1 Docker Desktop 환경

Ingress Controller가 localhost에 직접 바인딩되는 경우:

```
http://localhost/api/shorten
http://localhost/api/redirect
```

---

### 3.2 port-forward 방식

환경 독립적으로 가장 안정적인 테스트 방식이다.

```
kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8080:80
```

```
http://localhost:8080/api/shorten
http://localhost:8080/api/redirect
```

---

## 4. 구조

Ingress 기반 요청 흐름은 다음과 같다.

```
Client
  ↓
Ingress Controller
  ↓
Ingress Rules
  ↓
gateway-service
  ↓
shortener / redirect
```

---

## 5. 삭제

```
kubectl delete ingress gateway-ingress
```

---

## 6. 핵심 정리

Ingress는 외부 네트워크 진입점이 아니라 HTTP 라우팅 규칙이며,
실제 접근 가능 여부는 Ingress Controller의 노출 방식에 의해 결정된다.
