package domain.discountpolicy

import domain.reservations.items.ReservationInfo

interface DiscountCondition {
    fun isSatisfiedBy(info: ReservationInfo): Boolean
}

class TimeCondition : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean = true
}

class MovieDayCondition : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean = true
}
