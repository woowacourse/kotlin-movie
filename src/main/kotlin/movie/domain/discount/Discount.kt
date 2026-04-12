package movie.domain.discount

import movie.domain.Price
import movie.domain.Schedule

class Discount(
    private val policies: List<DateDiscountPolicy> = listOf(MovieDayPolicy(), TimePolicy()),
) {
    fun getTotalDiscountPrice(
        price: Price,
        schedule: Schedule,
    ): Price =
        policies.fold(price) { currentPrice, policy ->
            policy.discount(currentPrice, schedule)
        }
}
