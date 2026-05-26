# 홍대병동 Backend

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/34bba425-7a8e-4de5-8e37-ab0f97104cc4" />

---

비주류 음악을 발굴하고, 나만의 DIG를 기록하고 공유하는 서비스의 백엔드 API 서버입니다.

![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-Build-02303A?style=flat-square&logo=gradle&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-Hibernate-59666C?style=flat-square&logo=hibernate&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?style=flat-square&logo=mysql&logoColor=white)

---

## Backend Team

| 이름 | 역할 |
| --- | --- |
| 성현준 | Backend |
| 김우진 | Backend |

## Overview

홍대병동 백엔드 서버는 사용자 인증, 음악 검색, DIG 생성 및 조회 기능을 제공하는 Spring Boot 기반 API 서버입니다.  
YouTube API를 통해 음악을 검색하고, Gemini API를 활용해 음악 메타데이터를 처리합니다.

## Features

| 기능 | 설명 |
| --- | --- |
| Auth | 회원가입 및 로그인 |
| Song Search | YouTube 기반 음악 검색 |
| Metadata | Gemini 기반 음악 메타데이터 처리 |
| DIG | DIG 생성, 상세 조회, 목록 조회 |
| DIG Search | DIG 검색 및 갱신 |
| Common Response | 공통 API 응답 및 예외 처리 |

## Tech Stack

| 분류 | 기술 |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Database | H2, MySQL |
| ORM | Spring Data JPA |
| Validation | Spring Validation |
| API Docs | Springdoc OpenAPI, Swagger UI |
| Build Tool | Gradle |
| External API | YouTube API, Gemini API |

## Architecture

### ERD

<img width="2474" height="1534" alt="Nerdinary_taem_o_BE_ERD" src="https://github.com/user-attachments/assets/c9e755a9-9eba-4f21-9439-1405128f8109" />

### System Architecture

<img width="1892" height="1004" alt="Nerdinary_taem_o_BE_DeployDiagram" src="https://github.com/user-attachments/assets/907485e3-f47c-402b-a77f-2c9a0b97af64" />

## Project Structure

```text
src/main/java/com/woojin/nerdinary_taem_o
├── api
│   ├── AuthController.java
│   ├── DigController.java
│   ├── DigSearchController.java
│   └── SongController.java
├── common
│   ├── config
│   ├── dto
│   ├── entity
│   └── exception
└── domain
    ├── dig
    ├── song
    └── user
```

## Getting Started

### Requirements

- Java 17
- Gradle Wrapper
- MySQL 또는 H2

### Environment Variables

`.env.example`을 참고하여 프로젝트 루트에 `.env` 파일을 생성합니다.


### Run

Windows PowerShell:

```bash
.\gradlew.bat bootRun
```

macOS / Linux:

```bash
./gradlew bootRun
```


## API Docs

애플리케이션 실행 후 Swagger UI에서 API 문서를 확인할 수 있습니다.

```text
http://localhost:8080/swagger-ui.html
```
