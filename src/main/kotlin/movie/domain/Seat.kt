package movie.domain

class Seat(
    val seatNumber: SeatNumber,
    val rank: SeatRank,
) {
    fun getPrice(): Price = rank.price
}
