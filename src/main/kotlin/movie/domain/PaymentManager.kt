package movie.domain

import movie.domain.discount.DateDiscountPolicy
import movie.domain.discount.MovieDayPolicy
import movie.domain.discount.TimePolicy
import movie.domain.payment.PaymentMethod
import movie.domain.point.Point
import movie.domain.point.PointPolicy

class PaymentManager(
    private val discountPolicies: List<DateDiscountPolicy> = listOf(MovieDayPolicy(), TimePolicy()),
    private val pointPolicy: PointPolicy = PointPolicy(),
) {
    fun calculateFinalPrice(
        cart: Cart,
        usePoint: Point,
        paymentMethod: PaymentMethod,
    ): Price {
        val totalDiscountedPrice =
            cart.getReservations().fold(Price(0)) { total, reservation ->
                val basePrice = reservation.calculateTotalPrice()
                val discountedPrice = getTotalDiscountPrice(basePrice, reservation.schedule)
                total + discountedPrice
            }

        val totalPrice = pointPolicy.usePoint(totalDiscountedPrice, usePoint)

        return paymentMethod.paymentPrice(totalPrice)
    }

    private fun getTotalDiscountPrice(
        price: Price,
        schedule: Schedule,
    ): Price =
        discountPolicies.fold(price) { currentPrice, policy ->
            policy.discount(currentPrice, schedule)
        }
}
