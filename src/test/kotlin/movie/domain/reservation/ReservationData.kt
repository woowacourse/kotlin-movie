package movie.domain.reservation

import movie.data.SeatsData
import movie.domain.movie.Movie
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import java.time.LocalDateTime

object ReservationData {
    private val screen = Screen(1, SeatsData.seats)

    private val selectedSeats =
        SelectedSeats(
            Seats(
                setOf(
                    Seat("A", 1, SeatGrade.B),
                    Seat("C", 1, SeatGrade.S),
                ),
            ),
        )

    private val screening1 =
        Screening(
            1L,
            screen,
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 10, 0),
                LocalDateTime.of(2026, 1, 1, 12, 0),
            ),
            ReservedSeats(Seats(emptySet())),
        )

    private val screening2 =
        Screening(
            2L,
            screen,
            ScreeningDateTime(
                LocalDateTime.of(2026, 1, 1, 14, 0),
                LocalDateTime.of(2026, 1, 1, 16, 0),
            ),
            ReservedSeats(Seats(emptySet())),
        )

    val movie1 = Movie(id = 1, title = "F1 더 무비", screenings = Screenings(listOf(screening1)))
    val movie2 = Movie(id = 2, title = "토이 스토리", screenings = Screenings(listOf(screening2)))

    val reservations =
        listOf(
            Reservation(movie1, screening1, selectedSeats),
            Reservation(movie2, screening2, selectedSeats),
        )
}
