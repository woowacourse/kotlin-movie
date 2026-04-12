package movie.domain.payment

import movie.domain.amount.PaymentResult
import movie.domain.amount.Point
import movie.domain.amount.Price

class PriceCalculator {
    fun calculate(
        seatsTotalPrice: Price,
        point: Point,
        paymentMethod: PaymentMethod,
    ): PaymentResult {
        var totalPrice = seatsTotalPrice

        val usagePoint = point.usableAmount(totalPrice)
        totalPrice = totalPrice.minus(Price(usagePoint.value))
        totalPrice = paymentMethod.applyDiscount(totalPrice)

        return PaymentResult(
            totalPrice = totalPrice,
            usedPoint = usagePoint,
        )
    }
}
