package movie.domain.seat.rank

import movie.domain.Price

class ARank : SeatRank {
    override fun getPrice(): Price {
        return Price(15_000)
    }
}
