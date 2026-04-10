package model.policy

import model.screening.Screening

sealed interface DiscountPolicy {
    fun getDiscountEffect(screening: Screening): DiscountEffect
}
