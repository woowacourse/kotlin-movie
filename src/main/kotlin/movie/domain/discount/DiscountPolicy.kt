package movie.domain.discount

import movie.domain.Price
import java.time.LocalDate
import java.time.LocalTime

class DiscountPolicy {
    private val discountCondition = DiscountCondition()

    fun calculateMovieDayDiscount(totalPrice: Price, date: LocalDate): Price {
        if (discountCondition.isMovieDay(date = date)) {
            val discountPrice = Price((totalPrice.value * 0.1).toInt())

            return totalPrice.minusPrice(discountPrice)
        }

        return totalPrice
    }

    fun calculateTimeDiscount(totalPrice: Price, startTime: LocalTime): Price {
        if (discountCondition.isTime(startTime = startTime)) {
            val discountPrice = Price(2000)

            return totalPrice.minusPrice(discountPrice)
        }

        return totalPrice
    }
}
