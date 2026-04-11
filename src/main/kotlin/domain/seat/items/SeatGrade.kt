package domain.seat.items

import domain.money.Money

enum class SeatGrade(private val price: Money) {
    GradeS(Money(SeatPrice.PRICE_S)),
    GradeA(Money(SeatPrice.PRICE_A)),
    GradeB(Money(SeatPrice.PRICE_B));

    fun getPrice(): Money {
        return this.price
    }
}

object SeatPrice { // 별도 객체는 enum 초기화 시점에 안전하게 참조 가능
    const val PRICE_S = 18000
    const val PRICE_A = 15000
    const val PRICE_B = 13000
}
