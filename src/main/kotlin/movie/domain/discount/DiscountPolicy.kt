package movie.domain.discount

import movie.domain.amount.Price
import java.time.LocalDateTime

interface DiscountPolicy {
    fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price
}

interface PercentageDiscountPolicy : DiscountPolicy

interface FixedAmountDiscountPolicy : DiscountPolicy
