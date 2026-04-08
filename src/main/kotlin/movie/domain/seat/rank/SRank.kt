package movie.domain.seat.rank

import movie.domain.Price

class SRank : SeatRank {
    override fun getPrice(): Price {
        return Price(18_000)
    }
}
