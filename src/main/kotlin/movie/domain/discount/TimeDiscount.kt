package movie.domain.discount

import movie.domain.amount.Money
import movie.domain.screening.ScreeningDateTime

class TimeDiscount : DiscountPolicy {
    override fun applyDiscount(
        price: Money,
        dateTime: ScreeningDateTime,
    ): Money {
        if (dateTime.isTimeDiscountTarget()) {
            return price.minus(Money(2000))
        }
        return price
    }
}
