package model.discount.reserveDiscountPolicy

import java.time.LocalDateTime

// 시간 할인
class TimeDiscountPolicy : ReserveDiscountPolicy {
    private val notDiscountTime = listOf(11, 12, 13, 14, 15, 16, 17, 18, 19)

    override fun calculatePrice(
        price: Int,
        reservedDateTime: LocalDateTime,
    ): Int {
        if (reservedDateTime.hour in notDiscountTime) {
            return price
        }
        return price - 2000
    }
}
