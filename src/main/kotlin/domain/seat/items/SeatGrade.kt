package domain.seat.items

import domain.money.Money

abstract class SeatGrade(
    private val seatGradeName: SeatGradeName,
    private val price: Money,
) {
    fun addPrice(money: Money): Money = price + money
}

class GradeS : SeatGrade(SeatGradeName("S"), Money(18_000))

class GradeA : SeatGrade(SeatGradeName("A"), Money(15_000))

class GradeB : SeatGrade(SeatGradeName("B"), Money(12_000))

class SeatGradeName(
    private val name: String,
)
