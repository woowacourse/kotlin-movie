package domain.discount

import domain.Money
import domain.screening.Screening

sealed interface DiscountPolicy {
    fun calculateDiscountAmount(price: Money, screening: Screening): Money
}
