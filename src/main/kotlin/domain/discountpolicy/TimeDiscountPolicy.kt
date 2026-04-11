package domain.discountpolicy

import domain.money.Money
import domain.reservations.items.Reservation

interface DiscountPolicy {
    val condition: DiscountCondition

    fun applyDiscount(
        price: Money,
        reservation: Reservation,
    ): Money
}

class TimeDiscountPolicy(
    override val condition: DiscountCondition,
    private val discountAmount: Money
) : DiscountPolicy {

    override fun applyDiscount(
        price: Money,
        reservation: Reservation,
    ): Money {
        val info = reservation.getReservationInfo()
        if (condition.isSatisfiedBy(info)) {
            return price - discountAmount
        }
        return price
    }
}

class MovieDayDiscountPolicy(
    override val condition: DiscountCondition,
    private val discountRate: Double
) : DiscountPolicy {

    override fun applyDiscount(
        price: Money,
        reservation: Reservation,
    ): Money {
        val info = reservation.getReservationInfo()
        if (condition.isSatisfiedBy(info)) {
            return price * discountRate
        }
        return price
    }
}
