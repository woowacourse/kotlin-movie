package movie.domain.discount

import movie.domain.amount.Price
import java.time.LocalDateTime

class MovieDayDiscount : PercentageDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price {
        if (localDateTime.dayOfMonth == 10 || localDateTime.dayOfMonth == 20 || localDateTime.dayOfMonth == 30) {
            return price.percentOf(90)
        }
        return price
    }
}
