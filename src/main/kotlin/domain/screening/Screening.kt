package domain.screening

import domain.common.TimeRange
import domain.movie.Movie
import domain.seat.ReserveState
import domain.seat.SeatPosition
import domain.seat.SeatPositions
import domain.seat.Seats
import java.time.LocalDateTime

class Screening(
    val id: Long? = null,
    val movie: Movie,
    val room: ScreeningRoom,
    val startTime: LocalDateTime,
    val seats: Seats = room.seats
) {

    fun isOverlapping(other: Screening): Boolean {
        return this.screenTimeRange.isOverlapping(other.screenTimeRange)
    }

    fun isSame(other: Screening): Boolean {
        return this.id == other.id
    }

    val screenTimeRange: TimeRange =
        TimeRange(
            startTime.toLocalTime(),
            startTime.toLocalTime()
                .plusMinutes(movie.runningTime.duration.toLong()),
        )

    init {
        require(
            screenTimeRange.start.isBefore(room.operatingTime.start).not() &&
                screenTimeRange.end.isBefore(room.operatingTime.end),
        ) {
            "상영 시간이 상영관의 운영시간에 포함되지 않습니다."
        }
    }

    fun reserve(position: SeatPosition): Screening =
        Screening(
                id = id,
                movie = movie,
                room = room,
                startTime = startTime,
                seats = seats.updateState(position, ReserveState.RESERVED)
        )

    fun isReservable(positions: SeatPositions) {
        require(positions.positions.all { seats.isReservable(it) }) { "이미 예약된 좌석입니다." }
    }
}
