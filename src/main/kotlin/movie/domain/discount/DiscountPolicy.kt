package movie.domain.discount

import movie.domain.amount.Money
import movie.domain.screening.ScreeningDateTime

interface DiscountPolicy {
    fun applyDiscount(
        price: movie.domain.amount.Money,
        dateTime: movie.domain.screening.ScreeningDateTime,
    ): movie.domain.amount.Money
}
