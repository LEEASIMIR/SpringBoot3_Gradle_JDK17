# .rest 파일 기본 사용법
## 파일 생성
프로젝트의 적절한 위치에  
api-test.rest와 같은 확장자가  
.http 또는 .rest인 파일을 생성합니다.

요청 작성  
파일 내부에 HTTP 요청을 작성합니다.  
각 요청은 # 또는 ###로 구분합니다.

GET 요청 예시:
```
GET http://localhost:8080/api/users
```

POST 요청 예시:
```
POST http://localhost:8080/api/users
Content-Type: application/json

{
"name": "홍길동",
"email": "hong@example.com"
}
```
요청 헤더 추가:
```
GET http://localhost:8080/api/users/1
Accept: application/json
Authorization: Bearer <your_token>
```
### 요청 실행
요청 옆에 있는 실행(▶) 아이콘을 클릭하거나,  
Alt + Enter를 누른 후 Run을 선택하여 실행합니다.  
실행 결과는 IDE 하단에 있는  
'Run' 또는 'Services' 탭에 표시됩니다.

### .rest 파일의 고급 기능
환경 변수: 여러 환경(개발, 스테이징, 프로덕션)에 따라  
URL이나 인증 토큰 등을 다르게 설정할 수 있습니다.  
http-client.env.json 파일을 생성하여 환경 변수를 정의합니다.
```
// http-client.env.json
{
    "development": {
        "host": "http://localhost:8080"
    },
    "production": {
        "host": "https://api.example.com"
    }
}
```
.rest 파일에서는 {{host}}와 같이 변수를 사용합니다.
```
GET {{host}}/api/users
```
### 응답 체인(Response Chain)  
이전 요청의 응답을 다음 요청에 활용할 수 있습니다.  
예를 들어, 로그인 API의 응답에서 받은 토큰을  
다음 API 요청의 헤더에 자동으로 추가할 수 있습니다.

### 로그인 요청
```
POST http://localhost:8080/login
Content-Type: application/json

{
"username": "user",
"password": "password"
}
```
> {% client.global.set("auth_token", response.headers.valueOf("X-Auth-Token")); %}

### 다음 API 요청
```
GET http://localhost:8080/api/data
Authorization: Bearer {{auth_token}}
```
.rest 파일은 코드와 함께 API 테스트 스크립트를 관리할 수 있어  
협업 시 유용하며, Git 같은 버전 관리 시스템에 통합하여  
API 변경사항에 대한 테스트를 쉽게 추적할 수 있는 장점이 있습니다.