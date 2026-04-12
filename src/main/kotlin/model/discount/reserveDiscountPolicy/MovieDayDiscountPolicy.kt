package model.discount.reserveDiscountPolicy

import model.seat.Price
import java.time.LocalDateTime

// 영화 할인 정책
class MovieDayDiscountPolicy : ReserveDiscountPolicy {
    private val discountDate = listOf(10, 20, 30)

    override fun calculatePrice(
        price: Price,
        reservedDateTime: LocalDateTime,
    ): Price {
        if (reservedDateTime.dayOfMonth in discountDate) {
            return Price((price.value * 0.9).toInt())
        }
        return price
    }
}
