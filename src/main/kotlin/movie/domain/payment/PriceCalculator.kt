package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.PaymentResult
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.reservation.Reservations

class PriceCalculator {
    fun calculate(
        discountPolicies: DiscountPolicies,
        reservations: Reservations,
        point: Point,
        paymentMethod: PaymentMethod,
    ): PaymentResult {
        var totalPrice = Money(0)

        reservations.forEach { reservation ->
            val discounted =
                discountPolicies.applyDiscount(
                    reservation.calculatePrice(),
                    reservation.getScreening().screeningDateTime,
                )
            totalPrice += discounted
        }

        val usagePoint = point.usableAmount(totalPrice)
        totalPrice = totalPrice.minus(Money(usagePoint.value))
        totalPrice = paymentMethod.applyDiscount(totalPrice)

        return PaymentResult(
            totalPrice = totalPrice,
            usedPoint = usagePoint,
        )
    }
}
