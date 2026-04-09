package domain.discountpolicy

import domain.reservations.items.ReservationInfo
import java.time.LocalTime

interface DiscountCondition {
    fun isSatisfiedBy(info: ReservationInfo): Boolean
}

class TimeCondition : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean =
        info.screenTime.isStartBefore(
            LocalTime.of(
                11,
                0,
            ),
        ) ||
            info.screenTime.isStartAfter(
                LocalTime.of(
                    20,
                    0,
                ),
            )
}

class MovieDayCondition : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean {
        val screeningDay = info.screenTime.screeningDayOfMonth()
        return screeningDay == 10 || screeningDay == 20 || screeningDay == 30
    }
}
