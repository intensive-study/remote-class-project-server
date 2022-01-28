# Getting Started

### docker compose로 실행 환경 세팅하기
1. `dev-tools/`의 경로에 .env 파일 생성하고, 아래의 환경 변수를 정의합니다.
   - MYSQL_DATABASE
   - MYSQL_USER
   - MYSQL_PASSWORD   
   - MYSQL_ROOT_PASSWORD

**ex)MYSQL_DATABASE=REMOTE_CLASS** 
   
2. `dev-tools` 의 경로에서 아래의 명령어로 docker compose를 실행합니다.  
*모든 실행은 `docker desktop`이 실행된 상태에서 이루어져야 합니다.*
```shell
docker compose up -d
```

### TokenProvider의 @Value 인식 에러 시
1. 인텔리제이에서 File-Settings를 클릭합니다.
2. Build, Execution, Deployment를 클릭합니다.
3. Annotation Processors에서 Enable annotation processing을 체크합니다.
4. apply 후 OK를 누르시면 됩니다.
