package model.payment

import model.reservation.MovieReservationGroup

data class MoviePaymentResult(
    val totalPrice: Money,
    val finalPrice: Money,
)

class DefaultMoviePayment(
    private val reservations: MovieReservationGroup,
    payType: PayType,
    point: Point,
) {
    private val sequentialMovieDiscount =
        SequentialMovieDiscount(
            listOf(
                MovieDayDiscount(),
                EarlyMorningDiscount(),
                LateNightDiscount(),
            ),
        )

    val sequentialPurchaseDiscount =
        SequentialPurchaseDiscount(
            listOf(
                PointDiscount(point),
                PayTypeDiscount(payType),
            ),
        )

    fun calculate(): MoviePaymentResult {
        val totalPrice =
            reservations.fold(Money(0)) { nextPrice, reservation ->
                nextPrice + reservation.seat.grade.price
            }
        val movieDiscountedPrice =
            reservations.fold(Money(0)) { nextPrice, reservation ->
                nextPrice + sequentialMovieDiscount.getDiscountedPrice(reservation)
            }
        return MoviePaymentResult(
            totalPrice = totalPrice,
            finalPrice = sequentialPurchaseDiscount.getDiscountedPrice(movieDiscountedPrice),
        )
    }
}
