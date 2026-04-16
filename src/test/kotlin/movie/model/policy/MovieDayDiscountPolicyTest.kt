@file:Suppress("NonAsciiCharacters")

package movie.model.policy

import movie.model.Screen
import movie.model.movie.Movie
import movie.model.movie.RunningTime
import movie.model.policy.PercentDiscount
import movie.model.screening.Screening
import movie.model.screening.ScreeningSeatMap
import movie.model.seat.Seat
import movie.model.seat.SeatGrade
import movie.model.seat.SeatNumber
import movie.model.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MovieDayDiscountPolicyTest {
    private val movie =
        Movie(
            title = "테스트 영화",
            runningTime = RunningTime(120),
        )
    private val screen = Screen("테스트관", Seats(listOf(Seat(SeatNumber('A', 1), SeatGrade.B))))

    private fun screeningOn(day: Int) =
        Screening(
            movie = movie,
            startDateTime = LocalDateTime.of(LocalDate.of(2025, 9, day), LocalTime.of(14, 0)),
            seatMap = ScreeningSeatMap(screen),
        )

    @Test
    fun `10일에 10퍼센트 할인이 적용된다`() {
        val discountEffect = MovieDayDiscountPolicy.findDiscount(screeningOn(10))

        assertThat(discountEffect).isInstanceOf(PercentDiscount::class.java)
        discountEffect as PercentDiscount
        assertThat(discountEffect.percent).isEqualTo(10)
    }

    @Test
    fun `20일에 10퍼센트 할인이 적용된다`() {
        val discountEffect = MovieDayDiscountPolicy.findDiscount(screeningOn(20))

        assertThat(discountEffect).isInstanceOf(PercentDiscount::class.java)
        discountEffect as PercentDiscount
        assertThat(discountEffect.percent).isEqualTo(10)
    }

    @Test
    fun `30일에 10퍼센트 할인이 적용된다`() {
        val discountEffect = MovieDayDiscountPolicy.findDiscount(screeningOn(30))

        assertThat(discountEffect).isInstanceOf(PercentDiscount::class.java)
        discountEffect as PercentDiscount
        assertThat(discountEffect.percent).isEqualTo(10)
    }

    @Test
    fun `해당하지 않는 날짜에는 할인이 적용되지 않는다`() {
        val discountEffect = MovieDayDiscountPolicy.findDiscount(screeningOn(1))

        assertThat(discountEffect).isInstanceOf(NoDiscount::class.java)
    }
}
