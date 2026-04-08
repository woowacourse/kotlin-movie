package domain.reservation

import domain.screening.Screen
import domain.screening.Screening
import domain.screening.ScreeningDateTime
import domain.seat.ReservatedSeats
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.Seats
import domain.seat.SelectedSeats
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
            1,
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
            2,
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
