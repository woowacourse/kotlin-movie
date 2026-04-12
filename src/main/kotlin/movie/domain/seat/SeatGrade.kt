package movie.domain.seat

import movie.domain.amount.Money

enum class SeatGrade(
    val price: Money,
) {
    S(Money(18000)),
    A(Money(15000)),
    B(Money(12000)),
}
