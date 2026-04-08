package model

import model.seat.toPrice
import java.time.LocalTime

class MoviePayment(
    val reservations: List<MovieReservationResult.Success>,
    val calculateTotalPrice: Int = reservations.sumOf { it.seat.grade.toPrice() },
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
            val day = reservation.screenTime.start.dayOfMonth
            if (day == 10 || day == 20 || day == 30) {
                currentPrice -= (reservation.seat.grade.toPrice() * 0.1).toInt()
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
            PayType.CREDIT_CARD -> (currentPrice * 0.95).toInt()
            PayType.CASH -> (currentPrice * 0.98).toInt()
        }

    fun applyPoint(point: Int) {
        currentPrice -= point
    }
}
