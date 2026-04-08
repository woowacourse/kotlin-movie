package movie.domain.seat.rank

import movie.domain.Price

class BRank : SeatRank {
    override fun getPrice(): Price {
        return Price(12_000)
    }
}
