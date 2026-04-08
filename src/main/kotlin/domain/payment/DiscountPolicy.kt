package domain.payment

import java.time.LocalDateTime

class DiscountPolicy {

    fun discount(date: LocalDateTime, money: Int): Int {
        return discountByTimeSale(
            date,
            discountByMovieDay(date.dayOfMonth, money)
        )
    }

    fun discountByMovieDay(day: Int, money: Int): Int {
        if (day == 10 || day == 20 || day == 30) {
            return (money * 0.9).toInt()
        }
        return money
    }

    fun discountByTimeSale(time: LocalDateTime, money: Int): Int {
        if (time.hour !in 11..19) {
            return money - 2000
        }
        return money
    }
}