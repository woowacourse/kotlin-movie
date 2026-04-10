package movie.domain.payment

import movie.domain.amount.PaymentResult
import movie.domain.amount.Point
import movie.domain.amount.Price
import movie.domain.discount.DiscountPolicies
import movie.domain.reservation.Reservations

class PriceCalculator {
    fun calculate(
        discountPolicies: DiscountPolicies,
        reservations: Reservations,
        point: Point,
        paymentMethod: PaymentMethod,
    ): PaymentResult {
        var totalPrice = reservations.discountedTotalPrice(discountPolicies)

        val usagePoint = point.usableAmount(totalPrice)
        totalPrice = totalPrice.minus(Price(usagePoint.value))
        totalPrice = paymentMethod.applyDiscount(totalPrice)

        return PaymentResult(
            totalPrice = totalPrice,
            usedPoint = usagePoint,
        )
    }
}
