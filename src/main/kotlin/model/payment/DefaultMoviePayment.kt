package model.payment

import model.reservation.MovieReservationGroup

data class MoviePaymentResult(
    val totalPrice: Money,
    val finalPrice: Money,
)

class DefaultMoviePayment(
    val reservations: MovieReservationGroup,
    val sequentialMovieDiscount: SequentialMovieDiscount,
    val pointDiscount: PaymentDiscountable,
    val payTypeDiscount: PaymentDiscountable?,
) {
    fun calculate(): MoviePaymentResult {
        val totalPrice =
            reservations.fold(Money(0)) { nextPrice, reservation ->
                nextPrice + reservation.seat.grade.price
            }
        val movieDiscountedPrice =
            reservations.fold(Money(0)) { nextPrice, reservation ->
                nextPrice + sequentialMovieDiscount.getDiscountedPrice(reservation)
            }
        val pointAppliedPrice = pointDiscount.applyDiscount(movieDiscountedPrice)
        return MoviePaymentResult(
            totalPrice = totalPrice,
            finalPrice = payTypeDiscount?.applyDiscount(pointAppliedPrice) ?: pointAppliedPrice,
        )
    }
}
