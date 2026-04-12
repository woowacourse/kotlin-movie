package model.discount.payDiscountPolicy

import model.seat.Price

// 할인 정책 인터페이스
interface PayDiscountPolicy {
    fun calculatePrice(price: Price): Price
}
