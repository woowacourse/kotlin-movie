package domain.discount

import domain.Money
import domain.movie.Movie
import domain.movie.RunningTime
import domain.screening.Screen
import domain.screening.Screening
import domain.seat.Seat
import domain.seat.SeatGrade
import domain.seat.SeatNumber
import domain.seat.Seats
import domain.movie.ShowingPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TimeDiscountPolicyTest {
    private val date = LocalDate.of(2025, 9, 15)

    private val movie = Movie(
        title = "테스트 영화",
        runningTime = RunningTime(120),
        showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2025, 9, 1),
            endDate = LocalDate.of(2025, 9, 30)
        )
    )

    private val screen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.B))))

    private fun screeningAt(startHour: Int, startMinute: Int = 0) = Screening(
        movie = movie,
        startDateTime = LocalDateTime.of(date, LocalTime.of(startHour, startMinute)),
        screen = screen
    )

    @Test
    fun `오전 11시 이전 상영에 2000원 할인이 적용된다`() {
        val discount = TimeDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningAt(10))
        assertThat(discount).isEqualTo(Money(2_000))
    }

    @Test
    fun `오후 8시 이후 상영에 2000원 할인이 적용된다`() {
        val discount = TimeDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningAt(21))
        assertThat(discount).isEqualTo(Money(2_000))
    }

    @Test
    fun `오후 8시 정각 상영에 2000원 할인이 적용된다`() {
        val discount = TimeDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningAt(20, 0))
        assertThat(discount).isEqualTo(Money(2_000))
    }

    @Test
    fun `해당하지 않는 시간대에는 할인이 적용되지 않는다`() {
        val discount = TimeDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningAt(14))
        assertThat(discount).isEqualTo(Money(0))
    }
}
