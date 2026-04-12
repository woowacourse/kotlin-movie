package movie.domain.discount

import movie.domain.amount.Price
import java.time.LocalDateTime
import java.time.LocalTime

class TimeDiscount : FixedAmountDiscountPolicy {
    override fun applyDiscount(
        price: Price,
        localDateTime: LocalDateTime,
    ): Price {
        val time = localDateTime.toLocalTime()
        if (time.isBefore(LocalTime.of(11, 0)) || time.isAfter(LocalTime.of(20, 0))) {
            return price.minus(Price(2000))
        }
        return price
    }
}
