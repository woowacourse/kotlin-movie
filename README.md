# 기능 구현 사항

## 데이터베이스 저장
- [x] 데이터베이스 연결하기
- [x] 상영중인 영화 정보를 데이터베이스에 저장한다
  - movie 테이블
  - id(PK), name, running_time_minutes
- [x] 영화 상영 정보를 데이터베이스에 저장한다
  - movie_screening 테이블
  - id(PK), movie_id(FK), screen_start, screen_end
- [x] 데이터베이스로부터 영화 정보 가져오기
- [x] 데이터베이스로부터 영화 상영 정보 가져오기
- [x] 전체 예매 정보를 데이터베이스에 저장한다
  - reservation 테이블
  - id(PK), total_price, used_point, payment_method
- [x] 좌석당 예매 정보를 데이터베이스에 저장한다
  - reservation_seat 테이블
  - id(PK), reservation_id(FK), seat

## API
- [x] 스프링 연동하기
- [x] 영화 상영 정보 조회 API 구현
- [x] 예매 API 구현