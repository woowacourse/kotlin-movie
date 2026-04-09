package domain.paycalculator

import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.PayMethod
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import domain.reservations.items.Reservation

class PayCalculator(
    private val reservations: List<Reservation>,
) {
    private var totalPrice: Money = Money(0)
    private val timeDiscountPolicy = TimeDiscountPolicy()
    private val movieDayDiscountPolicy = MovieDayDiscountPolicy()
    private val cardDiscountPolicy = CardDiscountPolicy()
    private val cashDiscountPolicy = CashDiscountPolicy()

    init {
        calculateTotalPrice()
    }

    fun calculateTotalPrice() {
        for (reservation in reservations) {
            totalPrice = movieDayDiscountPolicy.applyDiscount(totalPrice, reservation)
            totalPrice = timeDiscountPolicy.applyDiscount(totalPrice, reservation)
        }
    }

    fun usePoint(point: Int) {
        totalPrice - Money(point)
    }

    fun pay(payMethod: PayMethod): Money {
        totalPrice =
            when (payMethod) {
                PayMethod.CARD -> cardDiscountPolicy.applyDiscount(totalPrice)
                PayMethod.CASH -> cashDiscountPolicy.applyDiscount(totalPrice)
            }

        return totalPrice
    }
}
