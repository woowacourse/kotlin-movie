package domain.seat.items

import domain.money.Money

interface SeatGrade {
    val price: Money
}

class GradeS : SeatGrade {
    override val price: Money = Money(SeatPrice.PRICE_S)
}

class GradeA : SeatGrade {
    override val price: Money = Money(SeatPrice.PRICE_A)
}

class GradeB : SeatGrade {
    override val price: Money = Money(SeatPrice.PRICE_B)
}

object SeatPrice {
    const val PRICE_S = 18000
    const val PRICE_A = 15000
    const val PRICE_B = 13000
}
