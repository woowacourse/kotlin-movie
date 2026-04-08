package domain.payment

import domain.Money
import domain.Point
import domain.reservation.Reservations
import domain.screening.Screening
import domain.discount.DiscountPolicy

class PaymentSystem(
    private val discountPolicies: List<DiscountPolicy>,
    private val paymentMethod: PaymentMethod
) {
    fun pay(reservations: Reservations, point: Point): Money {
        val discountedTotal = reservations.fold(Money(0)) { total, reservation ->
            total + applyDiscounts(reservation.calculateBasePrice(), reservation.screening)
        }
        val afterPoint = discountedTotal - point.convertToMoney()
        return applyPaymentDiscount(afterPoint)
    }

    private fun applyDiscounts(price: Money, screening: Screening): Money =
        discountPolicies.fold(price) { current, policy ->
            current - policy.calculateDiscountAmount(current, screening)
        }

    private fun applyPaymentDiscount(price: Money): Money =
        price - paymentMethod.calculateDiscountAmount(price)
}
