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
        var totalPrice = reservations.totalPrice(discountPolicies)

        val usagePoint = point.usableAmount(totalPrice)
        totalPrice = totalPrice.minus(Money(usagePoint.value))
        totalPrice = paymentMethod.applyDiscount(totalPrice)

        return PaymentResult(
            totalPrice = totalPrice,
            usedPoint = usagePoint,
        )
    }
}
