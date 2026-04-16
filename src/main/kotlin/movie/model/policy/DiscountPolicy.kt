package movie.model.policy

import movie.model.screening.Screening

interface DiscountPolicy {
    val priority: Int

    fun findDiscount(screening: Screening): Discount
}
