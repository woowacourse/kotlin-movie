package model.discount.reserveDiscountPolicy

import java.time.LocalDateTime

interface ReserveDiscountPolicy {
    fun calculatePrice(
        price: Int,
        reservedDateTime: LocalDateTime,
    ): Int
}
