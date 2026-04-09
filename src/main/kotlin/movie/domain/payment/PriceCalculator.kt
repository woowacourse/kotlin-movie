package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicies
import movie.domain.screening.ScreeningDateTime

class PriceCalculator(
    private val ticketPrice: Money,
    private val screenDateTime: ScreeningDateTime,
    private val discountPolicies: DiscountPolicies,
    private val point: Point,
    private val paymentMethod: PaymentMethod,
) {
    fun calculate():Payment {
        var result = ticketPrice
        result = discountPolicies.applyDiscount(result, screenDateTime)
        val usagePoint = point.usableAmount(result)
        result = result.minus(Money(usagePoint.value))
        result = paymentMethod.applyDiscount(result)
        return Payment(result, usagePoint)
    }
}
