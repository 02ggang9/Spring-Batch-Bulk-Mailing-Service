## OverView

Spring-Batch-Bulk-Mailing-Service는 Spring Batch 프레임워크를 기반으로 만든 대량 메일 전송 시스템입니다.

많은 사용자에게 일괄적으로 동일한 내용의 메일을 보낼 때 사용하면 됩니다. 청크 단위로 사용자에게 메일을 보내며 예상치 못한 예외가 발생해도 Retry 기능을 통해 메일 전송을 재시도 합니다. 간단한 포맷팅을
지원하고 양식 또한 깔끔하고 정돈되어 있습니다.

## Quick Start Guide

### 1. Docker를 사용해 MYSQL 설치

Spring-Batch는 데이터베이스에 로그를 남길 수 있는 테이블이 없으면 실행이 되지 않기 때문에 필수적으로 데이터베이스를 설치 해야 합니다.

~~~yml
version: "3"
services:
  db:
    image: mysql:8.0
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 1234 # 사용자에 맞게 MYSQL ROOT 비밀번호 수정
      MYSQL_DATABASE: springBatch # 사용자에 맞게 이름 수정
      MYSQL_USER: user # 사용자에 맞게 이름 변경  
      MYSQL_PASSWORD: 1234 # 사용자에 맞게 비밀번호 변경
      TZ: Asia/Seoul
~~~

docker-compose.yml 파일 생성 후 docker-compose up 명령어를 사용해 도커 실행

### 2. 프로젝트 clone

~~~
git clone https://github.com/02ggang9/Spring-Batch-Bulk-Mailing-Service.git
~~~

### 3. application.yml 파일 추가

데이터베이스 연결과 이메일 연동, API 사용을 위한 패스워드를 설정하기 위해서 yml 파일을 추가합니다.

파일 경로는 spring-batch > src > main > resources > application.yml 입니다.

~~~yml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: # fix
    password: # fix
    properties:
      mail.smtp.starttls.enable: true
      mail.smtp.auth: true
    protocol: smtp
  datasource:
    url: jdbc:mysql://localhost:3306/{}?serverTimezone=Asia/Seoul&characterEncoding=UTF-8 # fix {}는 입력하고 제거할 것.
    username: user # fix
    password: 1234 # fix
    driver-class-name: com.mysql.cj.jdbc.Driver
  batch:
    jdbc:
      initialize-schema: always
    job:
      enabled: false
      name: ${job.name:NONE}
  jpa:
    hibernate:
      ddl-auto: update
    database-platform: org.hibernate.dialect.MySQL8Dialect
    defer-datasource-initialization: true
    properties:
      hibernate:
        format_sql: true
    show-sql: true

mailing.password: "" # fix
~~~

### 4. Database Table 생성

위의 설명대로 DB에 로그를 남기기 위한 테이블이 있어야 하며, 생성하기 위한 SQL 문은 아래와 같습니다.

~~~
-- Autogenerated: do not edit this file

CREATE TABLE BATCH_JOB_INSTANCE  (
                                     JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT ,
                                     JOB_NAME VARCHAR(100) NOT NULL,
                                     JOB_KEY VARCHAR(32) NOT NULL,
                                     constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION  (
                                      JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                      VERSION BIGINT  ,
                                      JOB_INSTANCE_ID BIGINT NOT NULL,
                                      CREATE_TIME DATETIME(6) NOT NULL,
                                      START_TIME DATETIME(6) DEFAULT NULL ,
                                      END_TIME DATETIME(6) DEFAULT NULL ,
                                      STATUS VARCHAR(10) ,
                                      EXIT_CODE VARCHAR(2500) ,
                                      EXIT_MESSAGE VARCHAR(2500) ,
                                      LAST_UPDATED DATETIME(6),
                                      constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                          references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
                                             JOB_EXECUTION_ID BIGINT NOT NULL ,
                                             PARAMETER_NAME VARCHAR(100) NOT NULL ,
                                             PARAMETER_TYPE VARCHAR(100) NOT NULL ,
                                             PARAMETER_VALUE VARCHAR(2500) ,
                                             IDENTIFYING CHAR(1) NOT NULL ,
                                             constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                 references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION  (
                                       STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                       VERSION BIGINT NOT NULL,
                                       STEP_NAME VARCHAR(100) NOT NULL,
                                       JOB_EXECUTION_ID BIGINT NOT NULL,
                                       CREATE_TIME DATETIME(6) NOT NULL,
                                       START_TIME DATETIME(6) DEFAULT NULL ,
                                       END_TIME DATETIME(6) DEFAULT NULL ,
                                       STATUS VARCHAR(10) ,
                                       COMMIT_COUNT BIGINT ,
                                       READ_COUNT BIGINT ,
                                       FILTER_COUNT BIGINT ,
                                       WRITE_COUNT BIGINT ,
                                       READ_SKIP_COUNT BIGINT ,
                                       WRITE_SKIP_COUNT BIGINT ,
                                       PROCESS_SKIP_COUNT BIGINT ,
                                       ROLLBACK_COUNT BIGINT ,
                                       EXIT_CODE VARCHAR(2500) ,
                                       EXIT_MESSAGE VARCHAR(2500) ,
                                       LAST_UPDATED DATETIME(6),
                                       constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                           references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
                                               STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                               SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                               SERIALIZED_CONTEXT TEXT ,
                                               constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                   references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
                                              JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT ,
                                              constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
                                          ID BIGINT NOT NULL,
                                          UNIQUE_KEY CHAR(1) NOT NULL,
                                          constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
                                         ID BIGINT NOT NULL,
                                         UNIQUE_KEY CHAR(1) NOT NULL,
                                         constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
                               ID BIGINT NOT NULL,
                               UNIQUE_KEY CHAR(1) NOT NULL,
                               constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);

CREATE TABLE `member` (
                          `id` bigint NOT NULL,
                          `email` varchar(255) NOT NULL,
                          `name` varchar(255) NOT NULL,
                          `push_agree` bit(1) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `member_seq` (
    `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

~~~

![테이블사진](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/reversing.png?raw=true)

### 5. 포스트맨 사용

- 회원 이름과 이메일 등록 (http://localhost:8080/member) POST 방식

~~~json
{
  "name": "ggang9",
  "email": "testtest1@gmail.com"
}
~~~

- 메시지 전송 (http://localhost:8080/mail/month-report) POST 방식

~~~json
{
  "jobName": "mailJob",
  "subject": "[Spring Batch News Letter]",
  "reportMessage": "{Spring Batch Test}\r\n\r\n안녕하세요. 02ggang9 입니다. \r\n\r\nREADME를 정성스럽게 봐 주셔서 정말로 감사드립니다. \r\n\r\n{1. 깔끔한 템플릿을 제공합니다.}\r\n(1) 이 템플릿은 주식회사 우아한형제들의 우아한테크 뉴스레터를 참고했습니다.\r\n(2) 포맷팅은 사용자의 설정으로 수정할 수 있습니다.\r\n\r\n{2. 기본적인 볼드체를 제공합니다.}\r\n볼드체를 사용하는 방법은 중괄호로 문자열을 감싸주시면 됩니다.\r\n\r\n성능적인 부분과 기타 개선 상황은 PR을 날려주신다면 확인하도록 하겠습니다.\r\n\r\n 감사합니다.",
  "password": "bdd1234"
}
~~~

![메일 이미지](https://github.com/02ggang9/02ggang9.github.io/blob/master/_posts/images/bdd/mail/%ED%85%9C%ED%94%8C%EB%A6%BF%EC%98%88%EC%8B%9C.png?raw=true)

## Customizing

### 메일 상단의 배너 수정 방법

1. springbatch > service > EmailService 이동
2. "<img" 키워드 검색 (Command + F) 첫번째 <img 태그의 src를 수정

### 하단의 이미지 클릭시 이동되는 경로 수정

1. springbatch > service > EmailService 이동
2. "<img" 키워드 검색 (Command + F) 2,3,4 번째 <img 태그의 src를 수정

## Performance

측정중..

## Update

### 2023.12.30

MVP 모델 업로드 및 README 작성

## License

Spring-Batch-Bulk-Mailing-Service는 MIT 라이센스에 따라 사용할 수 있습니다. 자세한 내용은 LICENSE 파일을 참조해주세요.

