package domain.discountpolicy

import domain.timetable.items.ScreenTime
import java.time.LocalTime

interface TimeDiscountCondition {
    fun isSatisfiedBy(screenTime: ScreenTime): Boolean
}

class TimeCondition : TimeDiscountCondition {
    override fun isSatisfiedBy(screenTime: ScreenTime): Boolean {
        val isMorning = screenTime.getStartTime() in MORNING_START_TIME..MORNING_END_TIME
        val isNight = screenTime.getStartTime() in NIGHT_START_TIME..NIGHT_END_TIME
        return isMorning || isNight
    }

    companion object {
        private val MORNING_START_TIME = LocalTime.of(0, 0)
        private val MORNING_END_TIME = LocalTime.of(11, 0)
        private val NIGHT_START_TIME = LocalTime.of(20, 0)
        private val NIGHT_END_TIME = LocalTime.of(23, 59)
    }
}

class DateCondition : TimeDiscountCondition {
    override fun isSatisfiedBy(screenTime: ScreenTime): Boolean {
        val day = screenTime.getDate().dayOfMonth
        return day in DAYS
    }

    companion object {
        private val DAYS = listOf(10, 20, 30)
    }
}
