package model.policy

import model.Money
import model.reservation.Reservations
import model.screening.Screening

class DiscountSystem(
    discountPolicies: List<DiscountPolicy>,
) {
    private val discountPolicies = discountPolicies.sortedBy { it.priority }

    fun discountPrice(reservations: Reservations): Money =
        reservations.fold(Money(0)) { total, reservation ->
            val basePrice = reservation.calculateBasePrice()
            val seatCount = reservation.seats.seatCount

            total + applyDiscounts(basePrice, reservation.screening, seatCount)
        }

    private fun applyDiscounts(
        price: Money,
        screening: Screening,
        seatCount: Int,
    ): Money =
        discountPolicies.fold(price) { current, policy ->
            val discount = policy.findDiscount(screening)
            current - discount.calculateDiscount(current, seatCount)
        }
}
