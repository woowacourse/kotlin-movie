package model.discount

import model.Money
import model.screening.Screening

sealed interface DiscountPolicy {
    fun calculateDiscountAmount(
        price: Money,
        screening: Screening,
    ): Money
}
