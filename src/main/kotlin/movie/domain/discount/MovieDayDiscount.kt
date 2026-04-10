package movie.domain.discount

import movie.domain.amount.Money
import java.time.LocalDateTime

class MovieDayDiscount : DiscountPolicy {
    override fun applyDiscount(
        price: Money,
        localDateTime: LocalDateTime,
    ): Money {
        if (localDateTime.dayOfMonth == 10 || localDateTime.dayOfMonth == 20 || localDateTime.dayOfMonth == 30) {
            return price.percentOf(90)
        }
        return price
    }
}
