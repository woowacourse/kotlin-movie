package model.payment

import model.Money
import model.Point
import model.discount.DiscountPolicy
import model.reservation.Reservations
import model.screening.Screening

class PaymentSystem(
    private val discountPolicies: List<DiscountPolicy>,
    private val paymentMethod: PaymentMethod
) {
    fun pay(reservations: Reservations, point: Point): PayResult {
        val discountedTotal = reservations.fold(Money(0)) { total, reservation ->
            total + applyDiscounts(reservation.calculateBasePrice(), reservation.screening)
        }
        val usedPoint = Point(point.value.coerceAtMost(discountedTotal.value))
        val finalPrice = discountedTotal - usedPoint.convertToMoney()
        return PayResult(applyPaymentDiscount(finalPrice), usedPoint)
    }

    private fun applyDiscounts(price: Money, screening: Screening): Money =
        discountPolicies.fold(price) { current, policy ->
            current - policy.calculateDiscountAmount(current, screening)
        }

    private fun applyPaymentDiscount(price: Money): Money =
        price - paymentMethod.calculateDiscountAmount(price)
}
