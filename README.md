# kotlin-movie

## 🚀 3단계 - 영화 예매(데이터베이스)

### 데이터베이스 스키마 설계

#### MOVIE (영화)

| 컬럼 | 타입 | 설명 |                                                    
  |---|---|---|
| id | BIGINT PK | 자동 증가 |                                            
| title | VARCHAR(255) | 영화 제목 |
| running_time | INT | 상영 시간 (분) |                                  
| start_date | DATE | 상영 시작 기간 |                                    
| end_date | DATE | 상영 종료 기간 |

#### SCREENING (상영 일정)

| 컬럼 | 타입 | 설명 |
  |---|---|---|                                                            
| id | BIGINT PK | 자동 증가 |
| movie_id | BIGINT FK | MOVIE 참조 |                                     
| start_time | TIMESTAMP | 상영 시작 시각 |
| end_time | TIMESTAMP | 상영 종료 시각 |                                 

#### RESERVATION (예매 내역)

| 컬럼 | 타입 | 설명 |                                                    
  |---|---|---|
| id | BIGINT PK | 자동 증가 |                                            
| payment_method | VARCHAR(50) | 결제 수단 (CREDIT_CARD 등) |
| used_point | INT | 사용 포인트 |                                        
| total_price | INT | 최종 결제 금액 |
| created_at | TIMESTAMP | 예매 일시 |                                    

#### RESERVATION_ITEM (예매 상세)

| 컬럼 | 타입 | 설명 |
  |---|---|---|                                                            
| id | BIGINT PK | 자동 증가 |
| reservation_id | BIGINT FK | RESERVATION 참조 |                         
| screening_id | BIGINT FK | SCREENING 참조 |
| seat_name | VARCHAR(10) | 좌석 이름 (예: C2) |                          

---                                                                       

### 기능 목록

#### 데이터베이스 초기화

- [x] 애플리케이션 실행 시 H2 연결 및 DDL 실행
- [x] 최초 실행 시 MockData를 DB에 적재

#### 영화 및 상영 조회

- [x] DB에서 영화 목록과 상영 정보를 조회하여 도메인 객체로 변환
- [x] 상영별 예약된 좌석 목록을 DB에서 조회하여 좌석 현황 출력

#### 예매 및 결제

- [x] 예매 요청 처리 시 `RESERVATION`, `RESERVATION_ITEM`을 하나의 트랜잭션으로 저장
- [x] 중복 예약 시도 시 예외 처리
- [x] 프로그램 재시작 후 DB에서 과거 예매 내역 조회

### 테스트 시나리오

#### MovieRepository

- [x] 존재하는 영화를 제목으로 조회하면 Movie 객체를 반환한다
- [x] 존재하지 않는 제목으로 조회하면 예외를 던진다

#### ScreeningRepository

- [x] 영화와 날짜로 상영 목록을 조회한다
- [x] 해당 날짜에 상영이 없으면 빈 리스트를 반환한다 
- [x] 예약된 좌석이 상영 조회 결과에 반영된다

#### ReservationRepository

- [x] 예매를 저장하면 RESERVATION과 RESERVATION_ITEM이 모두 저장된다
- [x] 이미 예약된 좌석을 다시 예약하면 예외가 발생한다
- [x] 예매 저장 중 예외 발생 시 트랜잭션이 롤백된다

---

## 🚀 4단계 - 영화 예매(프레임워크)

### 기능 목록

#### 애플리케이션 진입점
- [x] `@SpringBootApplication`으로 Spring Boot 애플리케이션 실행

#### 영화 목록 조회 API
- [x] `GET /api/movies` 요청 시 전체 영화 및 상영 정보를 JSON으로 반환
- [x] 응답에 영화 id, 제목, 상영 시간(분), 상영 목록(id, 시작/종료 시각) 포함

#### 예매 생성 API
- [x] `POST /api/reservations` 요청 시 예매를 생성하고 결과를 JSON으로 반환
- [x] 응답에 reservationId, 예매 목록, 사용 포인트, 결제 수단, 최종 금액 포함
- [x] 201 Created 상태 코드 반환

#### 예외 처리
- [x] 존재하지 않는 상영 ID로 예매 요청 시 적절한 오류 응답 반환
- [x] 이미 예약된 좌석으로 예매 요청 시 적절한 오류 응답 반환
- [x] 잘못된 JSON 형식 요청 시 400 Bad Request 반환

#### HTTP API 테스트
- [x] `GET /api/movies` 요청 시 200 OK를 반환한다
- [x] 영화 목록 조회 응답에 영화와 상영 정보가 올바르게 포함된다
- [x] `POST /api/reservations` 요청 시 201 Created를 반환한다
- [x] 예매 생성 응답에 reservationId와 totalPrice가 포함된다
- [x] 이미 예약된 좌석 예매 요청 시 적절한 오류 응답을 반환한다
- [x] 존재하지 않는 상영 ID로 예매 요청 시 적절한 오류 응답을 반환한다
- [x] 잘못된 요청 형식에 대해 400 Bad Request를 반환한다
