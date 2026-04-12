package model.discount.reserveDiscountPolicy

import java.time.LocalDateTime

// 영화 할인 정책
class MovieDayDiscountPolicy : ReserveDiscountPolicy {
    private val discountDate = listOf(10, 20, 30)

    override fun calculatePrice(
        price: Int,
        reservedDateTime: LocalDateTime,
    ): Int {
        if (reservedDateTime.dayOfMonth in discountDate) {
            return (price * 0.9).toInt()
        }
        return price
    }
}
