package domain

import java.time.LocalDateTime
import java.util.UUID

class Screening(
    val id: String = UUID.randomUUID().toString(),
    val movie: Movie,
    val room: ScreeningRoom,
    val startTime: LocalDateTime,
) {
    var seats = room.seats
        private set

    val screenTimeRange: TimeRange = TimeRange(
        startTime.toLocalTime(),
        startTime.toLocalTime().plusMinutes(movie.runningTime.duration.toLong())
    )

    init {
        require(
            screenTimeRange.start.isBefore(room.operatingTime.start).not() &&
                    screenTimeRange.end.isBefore(room.operatingTime.end),
        ) {
            "상영 시간이 상영관의 운영시간에 포함되지 않습니다."
        }
    }

    fun reserve(position: SeatPosition) {
        seats = seats.updateState(position, ReserveState.RESERVED)
    }
}
