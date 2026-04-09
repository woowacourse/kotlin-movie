package model.schedule

import model.CinemaTimeRange
import model.movie.Movie
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGroup
import model.seat.SeatRow
import java.util.Objects

class MovieScreening(
    val movie: Movie,
    val screenTime: CinemaTimeRange,
    val seatGroup: SeatGroup,
) {
    init {
        require(movie.isSameDuration(screenTime)) { "영화의 러닝타임과 상영관의 상영 시간이 일치하지 않습니다." }
    }

    override fun equals(other: Any?): Boolean {
        if (other is MovieScreening) {
            return movie == other.movie && screenTime == other.screenTime
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(movie.hashCode(), screenTime.hashCode())

    fun getSeat(
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): Seat = seatGroup.getSeat(seatRow, seatColumn)
}
