package model.schedule

import model.movie.Movie
import model.seat.Price
import model.seat.SeatInventory
import java.time.Duration
import java.time.LocalDateTime

data class Screening(
    val movie: Movie,
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime = startDateTime.plus(Duration.ofMinutes(movie.movieRunningTime)),
    val seatInventory: SeatInventory,
) {
    init {
        require(
            Duration.between(
                startDateTime,
                endDateTime,
            ) >= Duration.ofMinutes(movie.movieRunningTime),
        ) { "상영 시간이 영화 러닝타임보다 짧습니다" }
    }

    // 좌석을 예약하는 함수
    fun reserveSeats(seatNames: List<String>): Screening =
        Screening(
            movie = movie,
            startDateTime = startDateTime,
            seatInventory = seatInventory.reserveSeats(seatNames),
        )

    // 좌석의 총액을 계산하는 함수
    fun calculatePrice(seatNames: List<String>): Price = seatInventory.calculatePrice(seatNames)

    // 시간표 겹침을 검사하는 함수
    fun isOverlapping(screening: Screening): Boolean = startDateTime < screening.endDateTime && endDateTime > screening.startDateTime
}
