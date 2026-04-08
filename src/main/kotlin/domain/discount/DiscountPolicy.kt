package domain.discount

import domain.amount.Money
import domain.screening.ScreeningDateTime

interface DiscountPolicy {
    fun applyDiscount(
        price: Money,
        dateTime: ScreeningDateTime,
    ): Money
}
