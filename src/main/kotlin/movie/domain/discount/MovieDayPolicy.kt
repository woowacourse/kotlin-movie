package movie.domain.discount

import movie.domain.Price
import movie.domain.Schedule

class MovieDayPolicy : DateDiscountPolicy {
    override fun discount(
        price: Price,
        schedule: Schedule,
    ): Price {
        if (isMovieDay(schedule)) {
            return price.getDiscountPrice(0.1f)
        }

        return price
    }

    private fun isMovieDay(schedule: Schedule): Boolean = schedule.startTime.dayOfMonth in listOf(10, 20, 30)
}
