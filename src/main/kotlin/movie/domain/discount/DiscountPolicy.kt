package movie.domain.discount

import movie.domain.amount.Money
import movie.domain.screening.ScreeningDateTime

interface DiscountPolicy {
    fun applyDiscount(
        price: Money,
        dateTime: ScreeningDateTime,
    ): Money
}
