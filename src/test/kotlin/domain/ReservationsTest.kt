package domain

import domain.movie.Movie
import domain.movie.RunningTime
import domain.movie.ShowingPeriod
import domain.reservation.Reservations
import domain.screening.Screen
import domain.screening.Screening
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatNumber
import domain.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReservationsTest {
    private val date = LocalDate.of(2026, 4, 8)

    private val movie = Movie(
        title = "스파이더맨",
        runningTime = RunningTime(120),
        showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2026, 4, 1),
            endDate = LocalDate.of(2026, 4, 30)
        )
    )

    private val defaultScreen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.S))))

    private fun screening(startHour: Int): Screening =
        Screening(movie, LocalDateTime.of(date, LocalTime.of(startHour, 0)), defaultScreen)

    @Test
    fun `빈 상태로 시작할 수 있다`() {
        val reservations = Reservations()

        assertThat(reservations.isEmpty()).isTrue()
    }

    @Test
    fun `시간이 겹치는 상영을 예매하면 예외가 발생한다`() {
        val reservation1 = screening(14).reserve(listOf(SeatNumber('A', 1)))
        val reservations = Reservations().addReservation(reservation1)

        assertThrows(IllegalArgumentException::class.java) {
            val reservation2 = screening(15).reserve(listOf(SeatNumber('A', 1)))
            reservations.addReservation(reservation2)
        }
    }

    @Test
    fun `시간이 겹치지 않는 상영을 예매할 수 있다`() {
        val reservation1 = screening(14).reserve(listOf(SeatNumber('A', 1)))
        val reservation2 = screening(16).reserve(listOf(SeatNumber('A', 1)))

        val reservations = Reservations()
            .addReservation(reservation1)
            .addReservation(reservation2)

        assertThat(reservations).hasSize(2)
    }
}
