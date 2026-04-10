package domain.seat

import domain.purchase.Price

class Seats(val seats: List<Seat>) {
    fun label(): String {
        return seats.joinToString(", ") { it.coordinate.toString() }
    }

    fun getAllPrice(): Price {
        return Price(seats.sumOf { it.grade.price })
    }
}
