package domain.discount

import domain.amount.Money
import domain.screening.ScreeningDateTime

interface DiscountPolicy {
    fun calculateDiscount(price: Money, dateTime: ScreeningDateTime): Money
}

