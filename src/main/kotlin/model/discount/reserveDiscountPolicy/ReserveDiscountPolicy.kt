package model.discount.reserveDiscountPolicy

import model.seat.Price
import java.time.LocalDateTime

interface ReserveDiscountPolicy {
    fun calculatePrice(
        price: Price,
        reservedDateTime: LocalDateTime,
    ): Price
}
