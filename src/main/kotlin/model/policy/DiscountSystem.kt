package model.policy

import model.Money
import model.reservation.Reservations
import model.screening.Screening

class DiscountSystem(
    val discountPolicies: List<DiscountPolicy> = listOf(MovieDayDiscountPolicy, TimeDiscountPolicy)
) {
    fun discountPrice(
        reservations: Reservations
    ): Money = reservations.fold(Money(0)) { total, reservation ->
        val basePrice = reservation.calculateBasePrice()
        val seatCount = reservation.seats.seatCount.toDouble()

        total + basePrice - applyDiscounts(basePrice, reservation.screening, seatCount)
    }

    private fun applyDiscounts(
        price: Money,
        screening: Screening,
        seatCount: Double,
    ): Money = discountPolicies.fold(price) { current, policy ->
        val discountEffect = policy.getDiscountEffect(screening)
        getDiscountPrice(current, discountEffect, seatCount)
    }

    private fun getDiscountPrice(
        price: Money,
        discountEffect: DiscountEffect,
        seatCount: Double,
    ): Money = when (discountEffect) {
        is AmountDiscountEffect -> discountEffect.amount * seatCount
        is RateDiscountEffect -> price * discountEffect.rate
        NoDiscountEffect -> Money(0)
    }
}
