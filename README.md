## 🚀 3단계 - 영화 예매(데이터베이스)

### 구현 전략

- 도메인 객체 변경을 최소화한다. `Screening` 생성자에 `reservedSeatNumbers` 기본값 파라미터 하나만 추가하여 DB 복원을 지원한다.
- ID는 도메인 객체에 두지 않고 Repository 레이어에서 관리한다.
- Repository 인터페이스는 `repository` 패키지에, JDBC 구현체는 `db` 패키지에 분리한다.
- 로컬 실행은 파일 기반 H2, 테스트는 In-Memory H2를 사용한다.

### 패키지 구조

```
model/          — 핵심 비즈니스 로직 (기존 유지)
view/           — 콘솔 입출력 (기존 유지)
controller/     — 흐름 제어 (기존 유지)
repository/     — Repository 인터페이스
db/             — JDBC 구현체, ConnectionManager, 스키마
```

### 기능 목록

#### 1. H2 의존성 추가 및 DB 스키마 설계

- `build.gradle.kts`에 H2 의존성 추가
- `schema.sql` 작성 (movies, screenings, reservations, reservation_seats 테이블)
- `data.sql` 작성 (초기 영화/상영 데이터)
- `ConnectionManager` 구현 (로컬/테스트 환경 분리)

#### 2. Movie/Screening 조회 Repository 구현

- `MovieRepository` 인터페이스 정의
- `ScreeningRepository` 인터페이스 정의
- `JdbcMovieRepository` 구현 (movies 테이블 조회)
- `JdbcScreeningRepository` 구현 (screenings 조회 + reservedSeatNumbers 복원)

#### 3. Reservation 저장 Repository 구현

- `Screening` 생성자에 `reservedSeatNumbers: Set<SeatNumber> = emptySet()` 추가
- `ReservationRepository` 인터페이스 정의
- `JdbcReservationRepository` 구현 (reservation + reservation_seats 저장)

#### 4. 콘솔 앱 DB 연동

- `Scheduler`를 DB 기반으로 교체 (`DbScheduler` 구현)
- `Application.kt` 수정 (DB 기반 의존성 주입)

#### 5. DB 연동 테스트 작성

- `JdbcMovieRepositoryTest` (영화 목록 조회)
- `JdbcScreeningRepositoryTest` (상영 조회, 예약 좌석 복원)
- `JdbcReservationRepositoryTest` (예약 저장, 중복 좌석 검증)

---

## 🚀 4단계 - 영화 예매(프레임워크)

### 구현 전략

- Spring Boot를 추가하되 기존 도메인 객체 및 Repository 변경을 최소화한다.
- 콘솔 앱(`Application.kt`)과 Spring Boot 앱(`MovieApplication.kt`)을 분리한다.
- 기존 JDBC Repository를 Spring `@Bean`으로 등록해 재사용한다.
- 요청/응답 DTO는 `api/dto` 패키지에 분리한다.

### 패키지 구조

```
model/              — 핵심 비즈니스 로직 (기존 유지)
repository/         — Repository 인터페이스 (기존 유지)
db/                 — JDBC 구현체 (기존 유지)
api/config/         — Spring Bean 설정
api/controller/     — HTTP API 컨트롤러
api/dto/            — 요청/응답 DTO
api/exception/      — 전역 예외 처리
```

### 기능 목록

#### 1. Spring Boot 의존성 추가 및 앱 구성

- `build.gradle.kts`에 Spring Boot 의존성 추가
- `MovieApplication.kt` 작성 (`@SpringBootApplication`)
- `AppConfig.kt` 작성 (Repository Bean 등록, DataInitializer 실행)
- `application.properties` 설정 (H2 파일 기반 datasource)

#### 2. GET /api/movies 엔드포인트 구현

- `ScreeningRepository`에 `findById` 추가
- `MovieQueryService` 구현 (영화+상영 ID 포함 조회)
- `MovieController` 구현
- `MoviesResponse`, `MovieResponse`, `ScreeningResponse` DTO 구현

#### 3. POST /api/reservations 엔드포인트 구현

- `ReservationController` 구현 (`201 Created`)
- `CreateReservationRequest`, `CreateReservationResponse` DTO 구현
- `GlobalExceptionHandler` 구현 (400 Bad Request 처리)

#### 4. HTTP API 테스트 작성

- `MovieApiTest` (200 OK, 응답 구조 검증)
- `ReservationApiTest` (201 Created, 결제 금액 검증, 예외 케이스)
