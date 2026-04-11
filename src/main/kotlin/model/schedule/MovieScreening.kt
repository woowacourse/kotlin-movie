package model.schedule

import model.movie.Movie
import model.reservation.MovieReservationResult
import model.seat.SeatGroup
import model.seat.SeatPosition
import model.seat.SeatState
import model.time.CinemaTimeRange
import java.util.Objects

class MovieScreening(
    val movie: Movie,
    val screenTime: CinemaTimeRange,
    val seatGroup: SeatGroup,
) {
    init {
        require(movie.isSameDuration(screenTime)) { "영화의 러닝타임과 상영관의 상영 시간이 일치하지 않습니다." }
    }

    fun reserve(seatPosition: SeatPosition): MovieReservationResult {
        val seat = seatGroup[seatPosition]
        return MovieReservationResult(
            movie = movie,
            startTime = screenTime.start,
            seat = seat,
            state = SeatState.RESERVED,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other is MovieScreening) {
            return movie == other.movie && screenTime == other.screenTime
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(movie.hashCode(), screenTime.hashCode())
}
