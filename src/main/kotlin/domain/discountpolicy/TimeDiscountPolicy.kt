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

class TimeDiscountPolicy : DiscountPolicy {
    override val condition: DiscountCondition = TimeCondition()

    override fun applyDiscount(
        price: Money,
        reservation: Reservation,
    ): Money {
        val info = reservation.getReservationInfo()
        if (condition.isSatisfiedBy(info)) {
            return price - Money(DiscountAmount.TIME_DISCOUNT)
        }
        return price
    }
}

class MovieDayDiscountPolicy : DiscountPolicy {
    override val condition: DiscountCondition = MovieDayCondition()

    override fun applyDiscount(
        price: Money,
        reservation: Reservation,
    ): Money {
        val info = reservation.getReservationInfo()
        if (condition.isSatisfiedBy(info)) {
            return price * DiscountAmount.MOVIE_DAY_DISCOUNT
        }
        return price
    }
}
