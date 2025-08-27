## Spring Logging

Spring Boot 애플리케이션의 종합적인 로깅 시스템을 구축하고, 로그 수집/분석을 위한 Grafana + Loki 스택을 연동한 프로젝트입니다.

<br />

### 프로젝트 개요

- 환경별 로그 출력 설정 (로컬/테스트 vs 개발/운영)
- Request/Response 파라미터 로깅 및 민감정보 마스킹
- Grafana + Loki + Promtail을 통한 로그 수집 및 시각화

<br />

### 프로젝트 구조

```text
spring-logging/
├── src/main/java/
│   └── com/logging/
│       ├── filter/                                          # 요청/응답 로깅 관련 필터
│       │   └── AbstractRequestResponseLoggingFilter.java    # (추상 클래스) Request/Response 로깅 공통 로직
│       │   └── ExceptionLoggingFilter.java                  # 운영 환경 : 예외 발생 시 Request/Response 로깅
│       │   └── StandardLoggingFilter.java                   # 로컬/개발/테스트 환경 : Request/Response 로깅
│       │   └── AccessLoggingFilter.java                     # 액세스 로그 필터
│       ├── matcher/
│       │   └── LogExclusionMatcher.java                     # 로깅 제외 URL 매칭
│       └── dto/
│           └── RequestContext.java
├── src/main/resources/
│   └── logback-spring.xml                                   # 환경별 Logback 설정
├── Dockerfile                                               # Docker 이미지 빌드 설정
├── docker-compose.yml                                       # 통합 Docker Compose 설정
├── loki-config.yml                                          # Loki 설정
├── promtail-config.yml                                      # Promtail 설정
```

<br />

### 주요 기능

#### 1. 환경별 로그 출력 설정

로컬/테스트 환경 (local, test)
- 콘솔 출력
- SQL 로그 활성화
- 파라미터 바인딩 로그 포함

개발/운영 환경 (dev, prod)
- 콘솔 + 파일 출력
- JSON 로그 파일 (로그 분석용)
- 로그파일 롤링 정책 적용 (크기 및 시간)

<br />

#### 2. Request/Response 파라미터 로깅 및 민감정보 마스킹

기본 기능
- HTTP 요청/응답 정보 자동 로깅
- 요청 URL, HTTP 메서드, 상태 코드
- 처리 시간 측정
- 요청/응답 파라미터 로깅

민감정보 보호
- 설정 가능한 민감정보 필드 목록 (MASKING_FIELDS)
- 자동 마스킹 처리 : [MASKED]
- 민감정보 보호 확인을 위해 description 필드 등록

환경별 로깅 정책
- 로컬/테스트/개발 : 모든 요청에 대한 상세 로깅
- 운영 : 예외 발생 시에만 요청/응답 파라미터 로깅

```text
AbstractRequestResponseLoggingFilter : >>> POST /api/v1/tasks
{
  "title" : "할 일 등록 기능 개발",
  "description" : "[MASKED]"
}
AbstractRequestResponseLoggingFilter : <<< 200 OK
{
  "status" : 200,
  "success" : true,
  "data" : {
    "id" : 1,
    "title" : "할 일 등록 기능 개발",
    "description" : "[MASKED]",
    "completed" : false
  }
}
com.logging.filter.AccessLoggingFilter   : [POST] /api/v1/tasks [200 OK] (331ms)
```

<br />

#### 3. Grafana + Loki + Promtail을 통한 로그 수집 및 시각화

Grafana + Loki + Promtail 스택
- Promtail: 로그 파일 수집
- Loki: 로그 데이터 저장 및 인덱싱
- Grafana: 로그 시각화 및 대시보드

<br />

### 4. 실행 방법

#### 파일 구조
```text
spring-logging/
├── build/
│   └── libs/
│       └── spring-logging-0.0.1-SNAPSHOT.jar
├── Dockerfile
├── docker-compose.yml
├── loki-config.yml  
├── promtail-config.yml
├── grafana/
│   └── provisioning/
│       └── datasources/
│           └── loki.yml
└── logs/
    ├── [LOG_FILE].json
    └── yyyy-MM-dd.[LOG_FILE].i.log
```

#### 디렉토리 생성

```shell
mkdir -p grafana/provisioning/datasources
mkdir -p logs
```

#### Docker Compose 실행

```shell
# 전체 스택 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f
```

#### 서비스 접속

- Spring Boot 애플리케이션: http://localhost:8080
- Grafana 대시보드: http://localhost:3000
  - Username: admin
  - Password: admin123
- Grafana 대시보드 설정
  - Home -> Connections -> Data sources
    - Name : Loki
    - URL : http://loki:3100
  - Home -> Explore
    - Label filters : job, spring-app 선택
    - Run query 실행

<br />

#### 참고 자료
- Claude.ai 대화 내용
