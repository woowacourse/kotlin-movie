package movie.domain.discount

import movie.domain.Price
import movie.domain.Schedule

interface DateDiscountPolicy {
    fun discount(
        price: Price,
        schedule: Schedule,
    ): Price
}
