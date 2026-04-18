# kotlin-movie

## 1단계 구현 기능

### Movie
- [x] 영화 제목을 가진다.
- [x] 러닝 타임을 가진다.

### Schedule
- [x] 영화 시작 시간을 가진다.
- [x] 영화 종료 시간을 가진다.
- [x] 상영 영화 정보를 가진다.
- [x] 다른 스케줄과 시간이 겹친다면 True를 안겹친다면 False를 반환한다.
- [x] 예매된 좌석 정보를 가진다.
- [x] Theater를 가진다.

### Schedules
- [x] 스케줄들을 반환한다.
- [x] 상영중인 영화 제목들을 반환한다.

### SeatNumber
- [x] 행을 가진다.
- [x] 열을 가진다.

### Seat
- [x] 좌석 번호를 가진다.
- [x] 좌석 등급을 가진다.

### Reservation
- [x] 스케줄을 가진다.
- [x] 선택된 좌석들을 가진다.
- [x] 총 좌석 금액을 반환한다.

### Reservations
- [x] 예매한 영화들의 Schedule 리스트를 갖는다
- [x] 영화 시간이 다른 스케줄과 같은지 체크한다.

### Cart
- [x] Reservations를 가진다.

### Payment
- [x] 결제 수단 별 할인율을 가진다.

### Discount
- [x] 할인 정책에 맞게 할인된 금액을 계산한다.

### PointPolicy
- [x] 포인트 사용 정책에 맞게 금액을 계산한다.

### Price
- [x] 가격 값을 가진다.

### Theater
- [x] 좌석 정보를 가진다.

### MovieManager
- [x] 특정 영화 시작시간을 반환한다.
- [x] 상영중인 영화 제목들을 반환한다.

### PaymentManager
- [x] 할인, 포인트 금액을 차감한 금액을 반환한다.

## 2단계 구현 기능

## View

### OutputView
- [x] 상영 목록을 출력한다.
- [x] 좌석배치도를 출력한다.
- [x] 예매 내역을 출력한다.

### InputView
- [x] 예매 시작 여부를 입력받는다.
- [x] 영화 제목을 입력받는다.
- [x] 날짜를 입력받는다.
- [x] 상영 회차를 입력받는다.
- [x] 예매할 좌석을 입력받는다.
- [x] 다른 영화를 추가할지 여부를 입력받는다.
- [x] 포인트 사용 여부를 입력받는다.
- [x] 결제 수단을 입력받는다.
- [x] 최종 결제 여부를 입력받는다.

### InputParser
- [x] Y or N를 파싱한다.
- [x] 날짜를 파싱한다.
- [x] Int 타입의 숫자를 파싱한다.
- [x] 예매할 좌석을 파싱한다.
- [x] 포인트를 파싱한다.

### InputValidator
- [x] Y or N를 검증한다.
- [x] 날짜를 검증한다.
- [x] Int 타입의 숫자를 검증한다.
- [x] 예매할 좌석을 검증한다.

## Controller

### MovieController
- [x] 영화 예매를 진행할 수 있다.

## 3단계 구현 기능

## Database
### movie
- [x] 영화 정보를 저장한다.
- [x] title, running_time을 가진다.
- [x] 여러 상영 정보를 가진다.

### schedule
- [x] 상영 정보를 저장한다.
- [x] movie_id, start_time, end_time을 가진다.
- [x] 하나의 영화에 속한다.
- [x] 여러 예약 정보를 가진다.

### reservation
- [x] 예약 정보를 저장한다.
- [x] schedule_id, total_price, reserved_at을 가진다.
- [x] 하나의 상영에 속한다.
- [x] 여러 좌석 정보를 가진다.

### reserved_seat
- [x] 예약된 좌석 정보를 저장한다.
- [x] reservation_id, schedule_id, seat_number를 가진다.
- [x] 하나의 예약에 속한다.
- [x] 동일한 상영에서 동일 좌석은 한 번만 예약되도록 UNIQUE 제약 조건을 가진다.

### 기능
- [x] 상영중인 스케줄를 DB에서 가져올 수 있다.
- [x] 예약을 저장할 수 있다.

## 4단계 구현 기능
- [x] API를 구현한다.
  - [x] 영화를 조회하는 API를 구현한다. (GET /api/movies)
  - [x] 영화를 예매하는 API를 구현한다. (POST /api/reservations)
