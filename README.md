# kotlin-movie

# 3단계 - 영화 예매(데이터베이스)

## 기능 목록

### DB 환경설정

- [x] Connection 제공
- [x] 테이블 생성 및 초기값 설정
    - 이미 존재하는 경우 Skip

### Schema

- [x] MovieTable
    - id, 제목, 상영 길이, 상영 기간
- [x] ScreeningRoomTable
    - id, 이름, 운영 시간
- [x] ScreeningTable
    - id, 영화 id, 상영관 id, 시작 시간
- [x] ScreeningRoomSeatTable
    - 상영관id, 행, 열
- [x] ReservationTable
    - id, 상영 id
- [x] ReservationSeatTable
    - 예약 id, 행, 열

### Repository

- [x] ScreeningRepository
    - [x] 전체 상영 스케줄 조회
- [x] ReservationRepository
    - [x] 예약 정보 저장

### API

- [x] `GET /api/movies` 영화 목록 조회
- [x] `POST /api/reservations` 예매 생성
    - [x] 예외 처리 (400 Bad Request)
        - 존재하지 않는 상영 ID
        - 유효하지 않은 결제 수단
        - 중복된 좌석
