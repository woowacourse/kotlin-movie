package domain.seat

import domain.Money

enum class SeatGrade(val price: Money){
    S(Money(18_000)),
    A(Money(15_000)),
    B(Money(12_000)),
}
