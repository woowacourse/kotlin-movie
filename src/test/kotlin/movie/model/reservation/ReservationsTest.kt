@file:Suppress("NonAsciiCharacters")

package movie.model.reservation

import movie.model.Screen
import movie.model.movie.Movie
import movie.model.movie.RunningTime
import movie.model.screening.Screening
import movie.model.screening.ScreeningSeatMap
import movie.model.seat.Seat
import movie.model.seat.SeatGrade
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ReservationsTest {
    private val date = LocalDate.of(2026, 4, 8)

    private val movie1 =
        Movie(
            title = "스파이더맨1",
            runningTime = RunningTime(120),
        )

    private val movie2 =
        Movie(
            title = "스파이더맨2",
            runningTime = RunningTime(120),
        )

    private val defaultScreen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.S))))

    private fun screening1(startHour: Int): Screening =
        Screening(
            movie = movie1,
            startDateTime = LocalDateTime.of(date, LocalTime.of(startHour, 0)),
            seatMap = ScreeningSeatMap(defaultScreen),
        )

    @Suppress("SameParameterValue")
    private fun screening2(startHour: Int): Screening =
        Screening(
            movie = movie2,
            startDateTime = LocalDateTime.of(date, LocalTime.of(startHour, 0)),
            seatMap = ScreeningSeatMap(defaultScreen),
        )

    @Test
    fun `빈 상태로 시작할 수 있다`() {
        val reservations = Reservations()

        Assertions.assertThat(reservations.isEmpty()).isTrue()
    }

    @Test
    fun `시간이 겹치는 상영을 예매하면 예외가 발생한다`() {
        val reservation1 = screening1(14).reserve(listOf(SeatNumber('A', 1)))
        val reservations = Reservations().add(reservation1)

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            val reservation2 = screening2(14).reserve(listOf(SeatNumber('A', 1)))
            reservations.add(reservation2)
        }
    }

    @Test
    fun `시간이 겹치지 않는 상영을 예매할 수 있다`() {
        val reservation1 = screening1(14).reserve(listOf(SeatNumber('A', 1)))
        val reservation2 = screening1(16).reserve(listOf(SeatNumber('A', 1)))

        val reservations = Reservations().add(reservation1).add(reservation2)

        Assertions.assertThat(reservations).hasSize(2)
    }
}
