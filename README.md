# 📚 Library Management System

이 프로젝트는 **Spring Boot** 기반의 온라인 도서 관리 시스템입니다.  
**RESTful API**를 제공하며, OpenAPI Specification(OAS)을 기반으로 API 문서를 생성합니다.

---

## 사전 준비 사항

이 프로젝트를 실행하기 위해서는 다음의 환경이 필요합니다:

### **필수 소프트웨어**
- [JDK 17+](https://adoptopenjdk.net/)
- [Gradle 7+](https://gradle.org/install/)
- [PostgreSQL 14+](https://www.postgresql.org/download/)
- [Redis](https://redis.io/download/)


### **PostgreSQL 설정**
1. PostgreSQL 서비스가 실행 중인지 확인합니다.
2. 데이터베이스를 생성합니다.
```sql
   CREATE DATABASE library_db;
   CREATE USER user WITH PASSWORD 'password';
   ALTER ROLE user SET client_encoding TO 'utf8';
   ALTER ROLE user SET default_transaction_isolation TO 'read committed';
   ALTER ROLE user SET timezone TO 'UTC';
   GRANT ALL PRIVILEGES ON DATABASE library_db TO user;
```


### **Redis 설정**
1. Redis를 설치하고 실행합니다.
```
redis-server
```
2. Redis가 정상적으로 실행 중인지 확인합니다.
```
redis-cli ping
```
----

<br>

## 실행 방법
1. OpenAPI 코드 생성
OpenAPI 스펙을 기반으로 API 코드 및 모델 클래스를 생성합니다.
```
./gradlew openApiGenerate
```

2. 환경변수 설정
데이터베이스 연결 및 JWT 인증을 위한 환경변수를 설정합니다.
```
export DB_ADDR="jdbc:postgresql://localhost:5432/library_db"
export DB_USERNAME="username"
export DB_PASSWORD="password"
export JWT_SECRET="ahdfgfdjgfafgdkajhfgkgfakgfkagfhagfdgfadshfgakhdgfhagfkj"
```

3. 애플리케이션 실행
환경변수를 반영하여 Spring Boot 애플리케이션을 실행합니다.
```
./gradlew bootRun
```
