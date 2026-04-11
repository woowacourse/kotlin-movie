package model.schedule

import model.CinemaTime
import model.MovieReservationResult
import model.movie.MovieName
import model.seat.SeatPosition
import model.seat.SeatState

class MovieReservationGroup(
    movieReservations: Set<MovieReservationResult>,
) : Iterable<MovieReservationResult> {
    init {
        require(movieReservations.all { it.state != SeatState.AVAILABLE }) {
            "예약 가능 상태의 예약 결과를 가질 수 없습니다. "
        }
    }

    private val movieReservationGroup = movieReservations.toSet()

    override fun equals(other: Any?): Boolean {
        if (other is MovieReservationGroup) return movieReservationGroup == other.movieReservationGroup
        return false
    }

    override fun hashCode(): Int = movieReservationGroup.hashCode()

    operator fun minus(other: MovieReservationGroup): MovieReservationGroup =
        MovieReservationGroup(movieReservationGroup - other.movieReservationGroup)

    override fun iterator(): Iterator<MovieReservationResult> = movieReservationGroup.iterator()

    fun reserve(
        cinemaSchedule: CinemaSchedule,
        movieName: MovieName,
        startTime: CinemaTime,
        seatPosition: SeatPosition,
    ): MovieReservationGroup {
        val movieSchedule = cinemaSchedule[movieName]
        val movieScreening = movieSchedule[startTime]
        val movieReservationResult = movieScreening.reserve(seatPosition)
        if (movieReservationGroup.any { movieReservationResult.isEqual(it.movie, it.startTime, it.seat) }) {
            throw IllegalArgumentException("이미 예약된 좌석입니다.")
        }
        return MovieReservationGroup(movieReservationGroup + movieReservationResult)
    }
}
