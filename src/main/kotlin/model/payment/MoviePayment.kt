package model.payment

import model.schedule.MovieReservationGroup
import java.time.LocalTime

class MoviePayment(
    val reservations: MovieReservationGroup,
    val totalPrice: Money =
        Money(
            reservations.sumOf {
                it.seat.grade.price
                    .toInt()
            },
        ),
    var currentPrice: Money = totalPrice,
) {
    init {
        if (currentPrice < 0) currentPrice = Money(0)
    }

    fun discount() {
        discountMovieDay()
        discountTime()
    }

    fun discountMovieDay() {
        reservations.forEach { reservation ->
            for (day in listOf(10, 20, 30)) {
                if (reservation.startTime.isSameDay(day)) {
                    currentPrice -= reservation.seat.grade.price applyRate 0.1
                }
            }
        }
    }

    fun discountTime() {
        reservations.forEach { reservation ->
            val time = reservation.startTime.toLocalTime()
            if (!time.isAfter(LocalTime.of(11, 0)) ||
                !time.isBefore(LocalTime.of(20, 0))
            ) {
                currentPrice -= Money(2000)
            }
        }
    }

    fun pay(payType: PayType): Money =
        when (payType) {
            PayType.CREDIT_CARD -> {
                currentPrice = currentPrice applyRate 0.95
                currentPrice
            }
            PayType.CASH -> {
                currentPrice = currentPrice applyRate 0.98
                currentPrice
            }
        }

    fun applyPoint(point: Point) {
        currentPrice -= point.toMoney()
    }
}
