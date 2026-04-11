@file:Suppress("NonAsciiCharacters")

package model.screening

import model.Money
import model.Screen
import model.movie.Movie
import model.movie.RunningTime
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningTest {
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

    private val defaultSeats =
        Seats(
            listOf(
                Seat(SeatNumber('A', 1), SeatGrade.B),
                Seat(SeatNumber('A', 2), SeatGrade.B),
                Seat(SeatNumber('B', 1), SeatGrade.S),
            ),
        )

    private val defaultScreen = Screen("테스트관", defaultSeats)

    private fun screening(startHour: Int): Screening =
        Screening(
            movie1,
            LocalDateTime.of(date, LocalTime.of(startHour, 0)),
            ScreeningSeatMap(defaultScreen),
        )

    private fun screening2(startHour: Int): Screening =
        Screening(
            movie2,
            LocalDateTime.of(date, LocalTime.of(startHour, 0)),
            ScreeningSeatMap(defaultScreen),
        )

    @Test
    fun `종료 시각은 시작 시각에 영화 상영 길이를 더한 값이다`() {
        val screening = screening(14)

        Assertions
            .assertThat(screening.endDateTime)
            .isEqualTo(LocalDateTime.of(date, LocalTime.of(16, 0)))
    }

    @Test
    fun `시작 시각과 상영 날짜가 올바르게 반환된다`() {
        val screening = screening(14)

        Assertions.assertThat(screening.startShowTime).isEqualTo(LocalTime.of(14, 0))
        Assertions.assertThat(screening.showDate).isEqualTo(date)
    }

    @Test
    fun `두 Screening의 시간이 겹치지 않으면 false를 반환한다`() {
        val screening1 = screening(14)
        val screening2 = screening(16)

        Assertions.assertThat(screening1.hasOverlappingWith(screening2)).isFalse()
    }

    @Test
    fun `두 Screening의 시간이 겹치면 true를 반환한다`() {
        val screening1 = screening(14)
        val screening2 = screening2(15)

        Assertions.assertThat(screening1.hasOverlappingWith(screening2)).isTrue()
    }

    @Test
    fun `한 예약 요청 내에서 중복된 좌석을 선택하면 예외가 발생한다`() {
        val screening = screening(14)

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            screening.reserve(listOf(SeatNumber('A', 1), SeatNumber('A', 1)))
        }
    }

    @Test
    fun `존재하지 않는 좌석을 예약하면 예외가 발생한다`() {
        val screening = screening(14)

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            screening.reserve(listOf(SeatNumber('E', 4)))
        }
    }

    @Test
    fun `예약 결과의 기본 가격이 올바르게 계산된다`() {
        val screening = screening(14)
        val reservation = screening.reserve(listOf(SeatNumber('A', 1), SeatNumber('B', 1)))

        Assertions.assertThat(reservation.calculateBasePrice()).isEqualTo(Money(12_000 + 18_000))
    }

    @Test
    fun `영화와 시작 시각이 같으면 같은 상영 일정으로 판단한다`() {
        val screening1 = screening(14)
        val screening2 = screening(14)

        Assertions.assertThat(screening1.isSameScreening(screening2)).isTrue()
    }

    @Test
    fun `영화나 시작 시각이 다르면 다른 상영 일정으로 판단한다`() {
        val screening1 = screening(14)
        val screening2 = screening(15)
        val screening3 = screening2(14)

        Assertions.assertThat(screening1.isSameScreening(screening2)).isFalse()
        Assertions.assertThat(screening1.isSameScreening(screening3)).isFalse()
    }
}
