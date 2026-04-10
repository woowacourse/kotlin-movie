package movie.domain.reservation

import movie.domain.movie.MovieTitle
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.Seat
import movie.domain.seat.SeatGrade
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import java.time.LocalDate
import java.time.LocalTime

object ReservationData {
    private val screen = Screen(1, Seats.createDefault())

    private val selectedSeats =
        SelectedSeats(
            listOf(
                Seat("A", 1, SeatGrade.B),
                Seat("C", 1, SeatGrade.S),
            ),
        )

    private val screening1 =
        Screening(
            MovieTitle("토이 스토리"),
            screen,
            ScreeningDateTime(
                LocalDate.of(2026, 1, 1),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
            ),
            ReservatedSeats(emptyList()),
        )

    private val screening2 =
        Screening(
            MovieTitle("F1 더 무비"),
            screen,
            ScreeningDateTime(
                LocalDate.of(2026, 1, 1),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
            ),
            ReservatedSeats(emptyList()),
        )

    val reservations =
        listOf(
            Reservation(screening1, selectedSeats),
            Reservation(screening2, selectedSeats),
        )
}
