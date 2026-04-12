package movie.domain.discount

import movie.domain.Price
import movie.domain.Schedule

class TimePolicy : DateDiscountPolicy {
    override fun discount(
        price: Price,
        schedule: Schedule,
    ): Price {
        if (isTimeDiscount(schedule)) {
            return price - Price(2000)
        }

        return price
    }

    private fun isTimeDiscount(schedule: Schedule): Boolean = schedule.startTime.hour !in 11..19
}
