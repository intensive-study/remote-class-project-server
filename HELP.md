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

3. .env파일을 등록시킵니다. (JUnit TEST 시에도 마찬가지입니다!)
- 인텔리제이에서 Edit configuration을 클릭합니다.
- envfile란을 클릭합니다.
- [만약 envfile 기능이 없다면 여기를 눌러 플러그인을 설치해 주세요](https://plugins.jetbrains.com/plugin/7861-envfile)
- 아래 사진처럼 등록한 뒤, Apply 후 OK를 누릅니다.
![env issue1](https://user-images.githubusercontent.com/46596758/151531761-170272a0-f0e2-4223-97e3-388118cff3dc.png)  
![env issue2](https://user-images.githubusercontent.com/46596758/151531772-556d8438-2294-4ec4-80f1-5619d514e859.png)

### TokenProvider의 @Value 인식 에러 시
1. 인텔리제이에서 File-Settings를 클릭합니다. (MAC의 경우 preference)
2. Build, Execution, Deployment를 클릭합니다.
3. Annotation Processors에서 Enable annotation processing을 체크합니다.
4. apply 후 OK를 누르시면 됩니다.

### Test 관련 issue 입니다.(추후에 HELP.MD 등에 옮기겠습니다)

1. 이슈 내용 : org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name
일단 테스트 시에는 스프링 부트에서 자체적으로 **test폴더** 안에 있는 application.yml 파일을 우선 참조합니다. 거기서 profile에 적어둔 aws, test이라는 패턴을 파악한 뒤 application-aws.yml, application-test.yml 파일을 참조합니다. application-aws.yml의 경우 test 파일에 없기 때문에 main 폴더의 application-aws.yml를 참조하고, application-test.yml의 경우 test 폴더 안의 파일을 참조합니다. 하지만 참조하는 application-test.yml에 jpa 및 db 관련 설정이 없었습니다. 관련 내용을 application-test.yml에 추가해 주었고, 해결되었습니다.
2. mysql access denied 이슈가 발생하였습니다. configuration에서 .env 추가해 줌으로써 해결하였습니다.
