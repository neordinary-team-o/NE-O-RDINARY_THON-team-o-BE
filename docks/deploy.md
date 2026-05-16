# Docker + GHCR + GitHub Actions 배포 가이드

## 배포 흐름

`main` 브랜치에 머지되면 GitHub Actions가 테스트를 실행하고 Docker 이미지를 GHCR에 push합니다.
이후 EC2에 SSH로 접속해 최신 이미지를 pull하고 `nerdinary-app` 컨테이너를 교체합니다.

```text
main push
-> GitHub Actions
-> ./gradlew test
-> Docker image build
-> GHCR push
-> EC2 docker pull
-> nerdinary-app container replace
-> Nginx reverse proxy
```

## GitHub Secrets

GitHub Repository Settings > Secrets and variables > Actions에 아래 값을 등록합니다.

```text
EC2_HOST=EC2 public IP 또는 도메인
EC2_USER=ubuntu
EC2_SSH_KEY=EC2 private key 전체 내용
```

## EC2 환경변수

EC2에 운영 환경변수 파일을 생성합니다.

```bash
mkdir -p ~/nerdinary
nano ~/nerdinary/.env
```

```env
SPRING_PROFILES_ACTIVE=prod
DB_JDBC_URL=jdbc:mysql://DB주소:3306/DB명
DB_USER=DB계정
DB_PASSWORD=DB비밀번호
JWT_SECRET=긴_랜덤_문자열
ACCESS_TOKEN_VALIDITY_IN_MILLISECONDS=3600000
REFRESH_TOKEN_VALIDITY_IN_MILLISECONDS=1209600000
```

## EC2 Docker 설치

Ubuntu 기준입니다.

```bash
sudo apt update
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker ubuntu
```

`usermod` 이후에는 SSH를 재접속합니다.

## GHCR private pull 로그인

GHCR 이미지를 private으로 둘 경우 EC2에서 한 번 로그인해야 합니다.
GitHub PAT는 `read:packages` 권한을 포함해야 합니다.

```bash
echo "GitHub_PAT" | docker login ghcr.io -u 깃허브아이디 --password-stdin
```

## Nginx 설정

Spring Boot 컨테이너는 EC2 내부 `127.0.0.1:8080`에만 바인딩됩니다.
Nginx가 외부 HTTP/HTTPS 요청을 컨테이너로 프록시합니다.

```nginx
server {
    listen 80;
    server_name 도메인_or_EC2_IP;

    location / {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

설정 확인 및 반영:

```bash
sudo nginx -t
sudo systemctl reload nginx
```

HTTPS는 도메인 DNS가 EC2를 바라보게 한 뒤 Certbot으로 인증서를 발급합니다.

## 보안그룹

```text
22    SSH    본인 IP만 허용
80    HTTP   전체 허용
443   HTTPS  전체 허용
8080  외부 오픈하지 않음
```

## 배포 확인

```bash
docker ps
docker logs nerdinary-app
curl http://127.0.0.1:8080
sudo nginx -t
```
