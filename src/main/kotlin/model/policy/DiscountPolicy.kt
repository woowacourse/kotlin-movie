package model.policy

import model.screening.Screening

interface DiscountPolicy {
    val priority: Int

    fun findDiscount(screening: Screening): Discount
}
