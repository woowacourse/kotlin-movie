package model.policy

import model.Money
import model.reservation.Reservations
import model.screening.Screening

class DiscountSystem(
    discountPolicies: List<DiscountPolicy> = listOf(MovieDayDiscountPolicy, TimeDiscountPolicy),
) {
    private val discountPolicies = discountPolicies.sortedBy { it.priority }

    fun discountPrice(reservations: Reservations): Money =
        reservations.fold(Money(0)) { total, reservation ->
            val basePrice = reservation.calculateBasePrice()
            val seatCount = reservation.seats.seatCount.toDouble()

            total + applyDiscounts(basePrice, reservation.screening, seatCount)
        }

    private fun applyDiscounts(
        price: Money,
        screening: Screening,
        seatCount: Double,
    ): Money =
        discountPolicies.fold(price) { current, policy ->
            val discountEffect = policy.getDiscountEffect(screening)
            current - getDiscountPrice(current, discountEffect, seatCount)
        }

    private fun getDiscountPrice(
        price: Money,
        discount: Discount,
        seatCount: Double,
    ): Money =
        when (discount) {
            is AmountDiscount -> discount.amount * seatCount
            is RateDiscount -> price * discount.rate
            NoDiscount -> Money(0)
        }
}
