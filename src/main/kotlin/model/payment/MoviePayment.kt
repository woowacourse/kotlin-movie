package model.payment

import model.MovieReservationResult
import java.time.LocalTime

class MoviePayment(
    val reservations: List<MovieReservationResult.Success>,
    val calculateTotalPrice: Int = reservations.sumOf { it.seat.price },
    var currentPrice: Int = calculateTotalPrice,
) {
    init {
        if (currentPrice < 0) currentPrice = 0
    }

    fun discount() {
        discountMovieDay()
        discountTime()
    }

    fun discountMovieDay() {
        reservations.forEach { reservation ->
            for (day in listOf(10, 20, 30)) {
                if (reservation.screenTime.start.isSameDay(day)) {
                    currentPrice -= (reservation.seat.price * 0.1).toInt()
                }
            }
        }
    }

    fun discountTime() {
        reservations.forEach { reservation ->
            val time = reservation.screenTime.start.toLocalTime()
            if (!time.isAfter(LocalTime.of(11, 0)) ||
                !time.isBefore(LocalTime.of(20, 0))
            ) {
                currentPrice -= 2000
            }
        }
    }

    fun pay(payType: PayType): Int =
        when (payType) {
            PayType.CREDIT_CARD -> {
                currentPrice = (currentPrice * 0.95).toInt()
                currentPrice
            }
            PayType.CASH -> {
                currentPrice = (currentPrice * 0.98).toInt()
                currentPrice
            }
        }

    fun applyPoint(point: Int) {
        currentPrice -= point
    }
}
