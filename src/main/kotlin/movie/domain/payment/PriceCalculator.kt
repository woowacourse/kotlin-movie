package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.reservation.Reservations

class PriceCalculator(
    private val discountPolicies: DiscountPolicies,
) {
    fun calculate(
        reservations: Reservations,
        point: Point,
        paymentMethod: PaymentMethod,
    ): PaymentResult {
        val totalPrice = reservations.totalPrice(discountPolicies)
        val usagePoint = point.usableAmount(totalPrice)
        val afterPoint = totalPrice - Money(usagePoint.value)
        val finalPrice = paymentMethod.applyDiscount(afterPoint)

        return PaymentResult(finalPrice, usagePoint, paymentMethod)
    }
}
