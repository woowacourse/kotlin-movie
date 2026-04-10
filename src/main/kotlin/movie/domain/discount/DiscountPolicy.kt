package movie.domain.discount

import movie.domain.amount.Money
import java.time.LocalDateTime

interface DiscountPolicy {
    fun applyDiscount(
        price: Money,
        localDateTime: LocalDateTime,
    ): Money
}
