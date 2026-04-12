package model.discount.reserveDiscountPolicy

import model.seat.Price
import java.time.LocalDateTime

// 시간 할인
class TimeDiscountPolicy : ReserveDiscountPolicy {
    private val notDiscountTime = listOf(11, 12, 13, 14, 15, 16, 17, 18, 19)

    override fun calculatePrice(
        price: Price,
        reservedDateTime: LocalDateTime,
    ): Price {
        if (reservedDateTime.hour in notDiscountTime) {
            return price
        }
        return Price(price.value - 2000)
    }
}
