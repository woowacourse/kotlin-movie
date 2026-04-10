package model.discount

import java.time.LocalDate
import java.time.LocalTime

class DiscountBenefits {
    // 무비데이 할인
    fun movieDay(
        price: Int,
        date: LocalDate,
    ): Int =
        if (date.dayOfMonth == 10 || date.dayOfMonth == 20 || date.dayOfMonth == 30) {
            price - (price * 0.1).toInt()
        } else {
            price
        }

    // 시간 할인
    fun timeDiscount(
        price: Int,
        time: LocalTime,
    ): Int = if (time.hour in 11..19) price else price - 2000

    // 포인트 할인
    fun pointDiscount(
        price: Int,
        usePoint: Int,
    ): Int {
        require(usePoint <= price) { "포인트 사용 금액은 결제 금액보다 클 수 없습니다" }
        return price - usePoint
    }

    // 카드 할인
    fun cardDiscount(price: Int): Int = price - (price * 0.05).toInt()

    // 현금 할인
    fun cashDiscount(price: Int): Int = price - (price * 0.02).toInt()
}
