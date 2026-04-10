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
        var totalPrice = Price(0)

        reservations.forEach { reservation ->
            val screeningDateTime = reservation.getScreening().screeningDateTime
            val discounted =
                discountPolicies.applyDiscount(
                    reservation.calculatePrice(),
                    screeningDateTime.date.atTime(screeningDateTime.startTime),
                )
            totalPrice += discounted
        }

        val usagePoint = point.usableAmount(totalPrice)
        totalPrice = totalPrice.minus(Price(usagePoint.value))
        totalPrice = paymentMethod.applyDiscount(totalPrice)

        return PaymentResult(
            totalPrice = totalPrice,
            usedPoint = usagePoint,
        )
    }
}
