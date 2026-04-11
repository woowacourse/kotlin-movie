package movie.domain.seat

import movie.domain.Price

class Seat(
    val seatNumber: SeatNumber,
    val rank: SeatRank,
) {
    fun getPrice(): Price = rank.price
}
