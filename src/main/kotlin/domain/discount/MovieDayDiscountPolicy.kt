package domain.discount

import domain.reservation.ReservedScreen

class MovieDayDiscountPolicy : DiscountPolicy {
    override fun discount(reservedScreen: ReservedScreen, money: Int): Int {
        if (checkMovieDay(reservedScreen.screen.startTime.value.dayOfYear)) return (money * 0.9).toInt()
        return money
    }

    private fun checkMovieDay(day: Int): Boolean {
        return day == 10 || day == 20 || day == 30
    }
}
