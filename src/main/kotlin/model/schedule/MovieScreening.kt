package model.schedule

import model.CinemaTime
import model.CinemaTimeRange
import model.movie.Movie
import model.reservation.MovieReservationResult
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGroup
import model.seat.SeatRow
import java.util.Objects

class MovieScreening(
    private val movie: Movie,
    val screenTime: CinemaTimeRange,
    val seatGroup: SeatGroup,
    reservedSeats: Set<Seat> = emptySet(),
) {
    private val reservedSeats: MutableSet<Seat> = reservedSeats.toMutableSet()

    init {
        require(movie.isSameDuration(screenTime)) { "영화의 러닝타임과 상영관의 상영 시간이 일치하지 않습니다." }
    }

    fun getSeat(
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): Seat? = seatGroup.getSeat(seatRow, seatColumn)

    fun reserve(
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ): MovieReservationResult {
        val seat = seatGroup.getSeat(seatRow, seatColumn) ?: return MovieReservationResult.Failed
        if (!reservedSeats.add(seat)) return MovieReservationResult.Failed
        return MovieReservationResult.Success(movie, screenTime, seat)
    }

    fun cancel(
        seatRow: SeatRow,
        seatColumn: SeatColumn,
    ) {
        val seat = seatGroup.getSeat(seatRow, seatColumn) ?: return
        reservedSeats.remove(seat)
    }

    fun conflictsWith(other: MovieReservationResult.Success): Boolean =
        screenTime != other.screenTime && screenTime.overlaps(other.screenTime)

    fun isOnDate(date: CinemaTime): Boolean = screenTime.start.isEqualDate(date)

    fun isWithin(period: CinemaTimeRange): Boolean = period.contains(screenTime.start) && period.contains(screenTime.end)

    fun overlaps(other: MovieScreening): Boolean = screenTime.overlaps(other.screenTime)

    fun isSameMovie(movie: Movie): Boolean = this.movie == movie

    override fun toString(): String = "$movie $screenTime"

    override fun equals(other: Any?): Boolean {
        if (other is MovieScreening) {
            return movie == other.movie && screenTime == other.screenTime
        }
        return false
    }

    override fun hashCode(): Int = Objects.hash(movie.hashCode(), screenTime.hashCode())
}
