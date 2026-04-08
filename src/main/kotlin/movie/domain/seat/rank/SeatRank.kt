package movie.domain.seat.rank

import movie.domain.Price

interface SeatRank {
    fun getPrice(): Price
}
