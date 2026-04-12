package movie.domain

import movie.domain.discount.Discount
import movie.domain.payment.Payment
import movie.domain.payment.PaymentMethod
import movie.domain.point.Point
import movie.domain.point.PointPolicy

class PaymentManager(
    private val discount: Discount = Discount(),
    private val pointPolicy: PointPolicy = PointPolicy(),
    private val payment: Payment = Payment()
) {
    fun calculateFinalPrice(
        cart: Cart,
        usePoint: Point,
        paymentMethod: PaymentMethod
    ): Price {
        val totalDiscountedPrice = cart.getReservations().fold(Price(0)) { total, reservation ->
            val basePrice = reservation.calculateTotalPrice()
            val discountedPrice = discount.getTotalDiscountPrice(basePrice, reservation.schedule)
            total + discountedPrice
        }

        val totalPrice = pointPolicy.usePoint(totalDiscountedPrice, usePoint)

        return payment.paymentPrice(paymentMethod, totalPrice)
    }
}
