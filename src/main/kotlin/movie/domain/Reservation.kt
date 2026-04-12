package movie.domain

import movie.domain.seat.SeatNumber

class Reservation(
    val schedule: Schedule,
    val seats: List<SeatNumber>,
) {
    fun isDuplicateTime(target: Schedule): Boolean = schedule.isDuplicateTime(target)

    fun calculateTotalPrice(): Price =
        seats.fold(Price(0)) { totalPrice, seatNumber ->
            totalPrice + schedule.getPrice(seatNumber)
        }
}
