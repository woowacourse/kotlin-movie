package model.policy

import model.screening.Screening

sealed interface DiscountPolicy {
    val priority: Int

    fun getDiscountEffect(screening: Screening): DiscountEffect
}
