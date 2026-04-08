package domain.payment

import domain.amount.Money
import domain.amount.Point
import domain.discount.DiscountPolicies
import domain.screening.ScreeningDateTime

class PriceCalculator(
    private val ticketPrice: Money,
    private val screenDateTime: ScreeningDateTime,
    private val discountPolicies: DiscountPolicies,
    private val point: Point,
    private val paymentMethod: PaymentMethod
) {
    fun calculate(): Payment {
        var result = ticketPrice
        result = discountPolicies.applyDiscount(result, screenDateTime)
        val usagePoint = point.usableAmount(result)
        result = result.minus(usagePoint)
        result = paymentMethod.applyDiscount(result)
        return Payment(result, usagePoint)
    }
}
