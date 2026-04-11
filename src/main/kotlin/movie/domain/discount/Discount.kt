package movie.domain.discount

import movie.domain.Price
import movie.domain.Schedule

class Discount {
    fun getTotalDiscountPrice(totalPrice: Price, schedule: Schedule): Price {
        val movieDayPrice = getMovieDayDiscountPrice(totalPrice, schedule)

        return getTimeDiscountPrice(movieDayPrice, schedule)
    }

    private fun getMovieDayDiscountPrice(totalPrice: Price, schedule: Schedule): Price {
        if (isMovieDay(schedule)) {
            return totalPrice.getDiscountPrice(0.1f)
        }

        return totalPrice
    }

    private fun isMovieDay(schedule: Schedule): Boolean {
        return schedule.startTime.dayOfMonth in listOf(10, 20, 30)
    }

    private fun getTimeDiscountPrice(totalPrice: Price, schedule: Schedule): Price {
        if (isTimeDiscount(schedule)) {
            return totalPrice - Price(2000)
        }

        return totalPrice
    }

    private fun isTimeDiscount(schedule: Schedule): Boolean {
        return schedule.startTime.hour !in 11..19
    }
}
