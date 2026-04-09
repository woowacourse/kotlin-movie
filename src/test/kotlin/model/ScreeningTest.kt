package model

import model.movie.Movie
import model.movie.RunningTime
import model.movie.ShowingPeriod
import model.screening.Screening
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningTest {
    private val date = LocalDate.of(2026, 4, 8)

    private val movie =
        Movie(
            title = "스파이더맨",
            runningTime = RunningTime(120),
            showingPeriod =
                ShowingPeriod(
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
        )

    private val defaultSeats =
        Seats(
            listOf(
                Seat(SeatNumber('A', 1), SeatGrade.B),
                Seat(SeatNumber('A', 2), SeatGrade.B),
                Seat(SeatNumber('B', 1), SeatGrade.S),
            ),
        )

    private val defaultScreen = Screen("테스트관", defaultSeats)

    private fun screening(startHour: Int): Screening = Screening(movie, LocalDateTime.of(date, LocalTime.of(startHour, 0)), defaultScreen)

    @Test
    fun `종료 시각은 시작 시각에 영화 상영 길이를 더한 값이다`() {
        val screening = screening(14)

        assertThat(screening.endDateTime).isEqualTo(LocalDateTime.of(date, LocalTime.of(16, 0)))
    }

    @Test
    fun `시작 시각과 상영 날짜가 올바르게 반환된다`() {
        val screening = screening(14)

        assertThat(screening.startShowTime).isEqualTo(LocalTime.of(14, 0))
        assertThat(screening.showDate).isEqualTo(date)
    }

    @Test
    fun `두 Screening의 시간이 겹치지 않으면 false를 반환한다`() {
        val screening1 = screening(14)
        val screening2 = screening(16)

        assertThat(screening1.isOverlapping(screening2)).isFalse()
    }

    @Test
    fun `두 Screening의 시간이 겹치면 true를 반환한다`() {
        val screening1 = screening(14)
        val screening2 = screening(15)

        assertThat(screening1.isOverlapping(screening2)).isTrue()
    }

    @Test
    fun `이미 예약된 좌석을 다시 예약하면 예외가 발생한다`() {
        val screening = screening(14)
        screening.reserve(listOf(SeatNumber('A', 1)))

        assertThrows(IllegalArgumentException::class.java) {
            screening.reserve(listOf(SeatNumber('A', 1)))
        }
    }

    @Test
    fun `한 예약 요청 내에서 중복된 좌석을 선택하면 예외가 발생한다`() {
        val screening = screening(14)

        assertThrows(IllegalArgumentException::class.java) {
            screening.reserve(listOf(SeatNumber('A', 1), SeatNumber('A', 1)))
        }
    }

    @Test
    fun `존재하지 않는 좌석을 예약하면 예외가 발생한다`() {
        val screening = screening(14)

        assertThrows(IllegalArgumentException::class.java) {
            screening.reserve(listOf(SeatNumber('E', 4)))
        }
    }

    @Test
    fun `정상 예약 후 availableSeats에서 해당 좌석이 제외된다`() {
        val screening = screening(14)
        screening.reserve(listOf(SeatNumber('A', 1)))

        val available = screening.availableSeats()

        assertThat(available.seatNumbers()).doesNotContain(SeatNumber('A', 1))
    }

    @Test
    fun `예약 결과의 기본 가격이 올바르게 계산된다`() {
        val screening = screening(14)
        val reservation = screening.reserve(listOf(SeatNumber('A', 1), SeatNumber('B', 1)))

        assertThat(reservation.calculateBasePrice()).isEqualTo(Money(12_000 + 18_000))
    }
}
