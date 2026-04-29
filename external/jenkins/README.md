# Jenkins 관련 명령어 정리

## 1. Jenkins 실행

```
docker compose --env-file .env.jenkins up -d
```

---

## 2. Jenkins 상태 확인

```
docker logs jenkins
```

---

## 3. 초기 admin password 확인

```
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

---

## 4. Jenkins 중지

```
docker compose --env-file .env.jenkins down  
```

---

## 5. Jenkins volume 확인

```
docker volume inspect tsidly_jenkins_home
```

---

## 6. Docker 사용 설정 (최초 1회)

### root로 컨테이너 접속

```
docker exec -u root -it jenkins bash
```

### Docker CLI 설치

```
apt-get update && apt-get install -y docker.io
```

### jenkins 유저를 docker 그룹에 추가

```
groupadd docker 2>/dev/null || true
usermod -aG docker jenkins
```

### 소켓 권한 확인 및 수정

```
chmod 666 /var/run/docker.sock
```

### yq 설치

```
curl -sSL https://github.com/mikefarah/yq/releases/latest/download/yq_linux_amd64 \
  -o /usr/local/bin/yq && chmod +x /usr/local/bin/yq
```

### 컨테이너에서 나가기

```
exit
```

### 컨테이너 재시작

설정 후 컨테이너를 재시작해야 그룹 변경이 반영된다.

```
docker restart jenkins
```

> 컨테이너를 재생성(`down` 후 `up`)하면 이 설정은 초기화되므로 다시 실행해야 한다.

---

## 7. Jenkins Credentials 설정

파이프라인에서 사용하는 Credentials은 Jenkins UI에서 등록한다.

**Jenkins → Manage Jenkins → Credentials → System → Global credentials → Add Credentials**

| ID | Kind | 설명 |
|---|---|---|
| `dockerhub-credentials` | Username with password | Docker Hub 계정 |
| `github-credentials` | Username with password | GitHub 계정 + PAT (repo 권한) |

> GitHub는 HTTPS push 시 비밀번호 대신 Personal Access Token을 사용해야 한다.
> GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
