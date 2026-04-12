package movie.domain.seat

import movie.domain.amount.Price

enum class SeatGrade(
    val price: Price,
) {
    S(Price(18000)),
    A(Price(15000)),
    B(Price(12000)),
}
