package domain

import java.time.LocalDateTime

class Screening(
    movie: Movie,
    room: ScreeningRoom,
    startTime: LocalDateTime
) {
    var seats = room.seats
        private set

    init {
        require(
            startTime.toLocalTime().isBefore(room.operatingTime.start).not() &&
                    (startTime.toLocalTime()
                        .plusMinutes(movie.runningTime.duration.toLong())).isBefore(room.operatingTime.end)
        ) {
            "상영 시간이 상영관의 운영시간에 포함되지 않습니다."
        }
    }

    fun reserve(position: SeatPosition) {
        seats = seats.updateState(position, ReserveState.RESERVED)
    }
}
