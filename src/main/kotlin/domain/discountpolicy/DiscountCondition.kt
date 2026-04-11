package domain.discountpolicy

import domain.reservations.items.ReservationInfo
import java.time.LocalTime

interface DiscountCondition {
    fun isSatisfiedBy(info: ReservationInfo): Boolean
}

class TimeCondition(
    private val beforeTime: LocalTime,
    private val afterTime: LocalTime
) : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean =
        info.screenTime.isStartBefore(beforeTime) || info.screenTime.isStartAfter(afterTime)
}

class MovieDayCondition(
    private val condition: List<Int>
) : DiscountCondition {
    override fun isSatisfiedBy(info: ReservationInfo): Boolean {
        val screeningDay = info.screenTime.screeningDayOfMonth()
        return screeningDay in condition
    }
}
