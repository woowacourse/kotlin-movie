package model.discount

import model.Money
import model.movie.Movie
import model.movie.RunningTime
import model.Screen
import model.screening.Screening
import model.seat.Seat
import model.seat.SeatGrade
import model.seat.SeatNumber
import model.seat.Seats
import model.movie.ShowingPeriod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MovieDayDiscountPolicyTest {
    private val movie = Movie(
        title = "테스트 영화",
        runningTime = RunningTime(120),
        showingPeriod = ShowingPeriod(
            startDate = LocalDate.of(2025, 9, 1),
            endDate = LocalDate.of(2025, 9, 30)
        )
    )

    private val screen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.B))))

    private fun screeningOn(day: Int) = Screening(
        movie = movie,
        startDateTime = LocalDateTime.of(LocalDate.of(2025, 9, day), LocalTime.of(14, 0)),
        screen = screen
    )

    @Test
    fun `10일에 10% 할인이 적용된다`() {
        val discount = MovieDayDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningOn(10))
        assertThat(discount).isEqualTo(Money(1_500))
    }

    @Test
    fun `20일에 10% 할인이 적용된다`() {
        val discount = MovieDayDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningOn(20))
        assertThat(discount).isEqualTo(Money(1_500))
    }

    @Test
    fun `30일에 10% 할인이 적용된다`() {
        val discount = MovieDayDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningOn(30))
        assertThat(discount).isEqualTo(Money(1_500))
    }

    @Test
    fun `해당하지 않는 날짜에는 할인이 적용되지 않는다`() {
        val discount = MovieDayDiscountPolicy.calculateDiscountAmount(Money(15_000), screeningOn(15))
        assertThat(discount).isEqualTo(Money(0))
    }
}
