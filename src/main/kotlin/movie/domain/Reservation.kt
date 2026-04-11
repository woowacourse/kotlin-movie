package movie.domain

class Reservation(
    val schedule: Schedule,
    val seats: List<Seat>,
) {
    fun isDuplicateTime(target: Reservation): Boolean {
        return schedule.isDuplicateTime(target.schedule)
    }

    fun calculateTotalPrice(): Price {
        return seats.fold(Price(0)) { totalPrice, seat ->
            totalPrice + seat.getPrice()
        }
    }
}
