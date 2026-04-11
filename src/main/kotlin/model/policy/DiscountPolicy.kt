package model.policy

import model.screening.Screening

interface DiscountPolicy {
    val priority: Int

    fun getDiscountEffect(screening: Screening): Discount
}
