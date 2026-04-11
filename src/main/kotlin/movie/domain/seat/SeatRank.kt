package movie.domain.seat

import movie.domain.Price

enum class SeatRank(val price: Price) {
    S(Price(18_000)),
    A(Price(15_000)),
    B(Price(12_000)),
}
