# 3단계 - 영화 예매(데이터베이스)

## 구현할 기능 목록
- [x] H2 의존성 추가
- [x] schema.sql 작성
- [x] DatabaseConnector 구현 (로컬/테스트 연결)
- [x] DatabaseInitializer 구현 (스키마 생성 + 초기 데이터)
- [x] DB 연결 테스트 작성
- [x] 도메인 객체에 DB용 ID 추가 
- [x] Repository 인터페이스 정의
- [x] JdbcReservedSeatRepository 구현 + 테스트
- [x] JdbcScreeningRepository 구현 + 테스트
- [x] JdbcMovieRepository 구현 + 테스트
- [x] JdbcReservationRepository 구현 + 테스트
- [x] 초기 데이터 INSERT (기존 MovieData → DB)
- [x] Controller가 Repository 사용하도록 변경
- [x] 통합 테스트 작성

#4단계 - 영화 예매(프레임워크)

## 구현할 기능 목록
- [x] Spring Boot 의존성 추가
- [x] ConsoleApplication과 Application 분리
- [x]  Application.kt — @SpringBootApplication 진입점 작성
- [x] Movies.kt에 toList() 추가
- [x] Screenings.kt에 toList() 추가
- [x] Screening.kt에 endTimeText() 추가
- [x] Movie.kt에 runningTimeMinutes 프로퍼티 추가
- [x] JdbcMovieRepository.kt SELECT에 running_time_minutes 추가
- [x] PaymentMethod.kt에 from(String) 오버로드 추가
- [x] AppConfig.kt — Connection, Repository, PriceCalculator를 Spring Bean으로 등록
- [x] MovieListResponse DTO 정의_
- [x] ReservationRequest DTO 정의
- [x] ReservationResponse DTO 정의
- [x] ErrorResponse DTO 정의
- [x] MovieApiController 구현 - GET /api/movies
- [x] ReservationApiController 구현 - GET /api/reservation
- [x] GlobalExceptionHandler 구현 — 에러 → HTTP 상태코드 매핑

### 테스트
- [x] 영화 목록 조회 시 200 OK 반환 테스트
- [x] 영화 목록 응답에 영화와 상영 정보가 포함되는 테스트
- [x] 예매 생성 시 201 Created 반환 테스트
- [x] 예매 응답에 reservationId와 totalPrice가 포함되는 테스트
- [x] 이미 예약된 좌석 예매 시 오류 응답 반환 테스트
- [x] 존재하지 않는 상영 ID로 예매 시 오류 응답 반환 테스트
- [x] 잘못된 요청 형식에 대해 오류 응답 반환 테스트
- [x] 시간이 겹치는 예매 시 오류 응답 반환 테스트
- [x] 기존 도메인 단위 테스트 유지 확인