package movie.domain.discount

import movie.MovieTitle
import movie.domain.Movie
import movie.domain.Price
import movie.domain.Schedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class DiscountTest {
    @Test
    fun `무비데이 할인이 적용된 가격을 반환한다`() {
        val schedule = Schedule(
            movie = Movie(
                title = MovieTitle("시동"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 12, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 15, 0),
        )
        val price = Price(30_000)
        val discount = Discount()
        val discountPrice = discount.getTotalDiscountPrice(price, schedule)

        assertThat(discountPrice).isEqualTo(Price(27_000))
    }

    @Test
    fun `타임 할인이 적용된 가격을 반환한다`() {
        val schedule = Schedule(
            movie = Movie(
                title = MovieTitle("시동"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 11, 7, 0),
            endTime = LocalDateTime.of(2026, 4, 11, 10, 0),
        )
        val price = Price(30_000)
        val discount = Discount()
        val discountPrice = discount.getTotalDiscountPrice(price, schedule)

        assertThat(discountPrice).isEqualTo(Price(28_000))
    }

    @Test
    fun `무비데이, 타임 할인이 동ㅇ시에 적용된 가격을 반환한다`() {
        val schedule = Schedule(
            movie = Movie(
                title = MovieTitle("시동"),
                runningTime = 120,
            ),
            startTime = LocalDateTime.of(2026, 4, 10, 7, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 0),
        )
        val price = Price(30_000)
        val discount = Discount()
        val discountPrice = discount.getTotalDiscountPrice(price, schedule)

        assertThat(discountPrice).isEqualTo(Price(25_000))
    }
}
