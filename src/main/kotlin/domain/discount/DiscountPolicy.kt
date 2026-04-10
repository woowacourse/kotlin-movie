package domain.discount

import domain.purchase.Price
import kotlinx.datetime.LocalDateTime

interface DiscountPolicy {
    fun isApplicable(at: LocalDateTime): Boolean
    fun discountApply(base: Price): Price
}
