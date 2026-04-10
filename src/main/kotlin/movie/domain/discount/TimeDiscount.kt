package movie.domain.discount

import movie.domain.amount.Money
import java.time.LocalDateTime
import java.time.LocalTime

class TimeDiscount : DiscountPolicy {
    override fun applyDiscount(
        price: Money,
        localDateTime: LocalDateTime,
    ): Money {
        val time = localDateTime.toLocalTime()
        if (time.isBefore(LocalTime.of(11, 0)) || time.isAfter(LocalTime.of(20, 0))) {
            return price.minus(Money(2000))
        }
        return price
    }
}
