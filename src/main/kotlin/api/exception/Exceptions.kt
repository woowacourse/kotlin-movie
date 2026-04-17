package api.exception

class ScreeningNotFoundException(
    id: Long,
) : RuntimeException("존재하지 않는 상영입니다. id=$id")

class SeatAlreadyReservedException : RuntimeException("이미 예약된 좌석입니다")
