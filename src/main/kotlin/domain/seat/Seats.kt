package domain.seat

class Seats(val seats: List<Seat>) {
    fun label(): String {
        return seats.joinToString(", ") { it.coordinate.toString() }
    }

    fun getAllPrice(): Int {
        return seats.sumOf { it.grade.price }
    }
}
