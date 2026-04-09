package movie.domain.discount

import movie.domain.amount.Money
import movie.domain.screening.ScreeningDateTime

class MovieDayDiscount : DiscountPolicy {
    override fun applyDiscount(
        price: Money,
        dateTime: ScreeningDateTime,
    ): Money {
        if (dateTime.isMovieDay()) {
            return price.percentOf(90)
        }
        return price
    }
}
