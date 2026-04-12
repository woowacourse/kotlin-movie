package model.discount.payDiscountPolicy

// 할인 정책 인터페이스
interface PayDiscountPolicy {
    fun calculatePrice(price: Int): Int
}
