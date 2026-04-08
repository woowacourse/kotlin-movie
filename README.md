# kotlin-movie

## 1단계 기능 구현 사항

## 도메인
### Point
* [x] Point 도메인을 구현한다.
  * [x] 포인트 정보를 가진다.

### User
* [] User 도메인을 구현한다.
  * [] 식별자 userId를 가진다.
  * [] 사용자 포인트 정보를 가진다.

### Movie
* [] Movie 도메인을 구현한다.
  * [] 영화 이름을 가진다.
  * [] 영화 ID를 가진다.

### ScreeningMovie
* [] ScreeningMovie 도메인을 구현한다.
  * [] 스크린 Id를 가진다.
  * [] Movie 정보를 가진다.
  * [] 상영 시작 / 종료 시간을 가진다.
  * [] seat 정보를 가진다.

### Theater
* [] Theater 도메인을 구현한다.
  * [] theaterId를 가진다.
  * [] 상영관 운영 시작 / 종료 시간을 가진다.
  * [] 좌석 목록을 가진다.

### TheaterScheduler
* [] TheaterScheduler 도메인을 구현한다.
  * [] 스크린별 상영 영화 정보를 가지고 있는다.
  * [] 특정 스크린 영화 상영 가능 여부를 검사한다.
  * [] 특정 스크린에 영화를 추가한다.

### Seat
* [] Seat 도메인을 구현한다.
  * [] 행(알파벳) 정보를 가진다.
  * [] 열(숫자) 정보를 가진다.
  * [] 좌석 등급(S, A, B)을 가진다.
  * [] 좌석 예약 여부(예약전, 예약중, 예약 완료)를 가진다.

### Reservation
* [] Reservation 도메인을 구현한다.
  * [] 예매한 영화 정보(ScreeningMovie)를 가진다.
  * [] 예매한 좌석 정보들을 가진다.

### Ticket
* [] Ticket 도메인을 구현한다.
  * [] 예매 목록(Reservation)을 가진다.
  * [] 총 결제 금액을 가진다.

### DiscountPolicy
* [] DiscountPolicy을 구현한다.
  * [] 무비데이 할인 정책 여부를 판단한다.
  * [] 시간 할인 정책 여부를 판단한다.

### DiscountCondition
* [] DiscountCondition 도메인을 구현한다.
  * [] 할인 정책에 따라 계산된 금액을 반환한다.

### PointCondition
* [] PointCondition 도메인을 구현한다.
  * [] 포인트 정책에 따라 계산된 금액을 반환한다.

### Payment
* [] Payment 도메인을 구현한다.
  * [] 결제 수단에 따른 할인을 적용한다.

### TicketClerk
* [] TicketClerk 도메인을 구현한다.
  * [] 영화 목록을 조회한다.
  * [] 특정 영화 정보를 조회한다.
  * [] 좌석 정보를 조회한다.
  * [] 예매를 진행한다.

### MovieLineup
* [] MovieLineup 도메인을 구현한다.
  * [] 상영 영화 목록들을 가진다.
  * [] 상영 중인 영화 제목들을 반환한다.
  * [] 특정 영화의 상영 시간 정보들을 반환한다.
