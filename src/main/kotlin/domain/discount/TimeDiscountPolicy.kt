package domain.discount

import domain.reservation.ReservedScreen
import domain.screening.ScreeningStartTime

class TimeDiscountPolicy: DiscountPolicy {
    override fun discount(
        reservedScreen: ReservedScreen,
        money: Int,
    ): Int {
        if(checkTimeSale(reservedScreen.screen.startTime)) return money - 2000
        return money
    }

    private fun checkTimeSale(time: ScreeningStartTime): Boolean {
        return time.value.hour !in 11..19
    }
}
