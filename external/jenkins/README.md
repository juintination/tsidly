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
