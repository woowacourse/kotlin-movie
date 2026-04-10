package domain.seat

import domain.purchase.Price

class Seats(val seats: List<Seat>) {
    fun label(): String {
        return seats.joinToString(", ") { it.coordinate.toString() }
    }

    fun getAllPrice(): Price {
        return Price(seats.sumOf { it.grade.price })
    }

    fun checkSeat(input: String): Seat {
        val coordinate = SeatCoordinate.from(input)

        val seat = seats.filter { it.coordinate.row == coordinate.row && it.coordinate.column == coordinate.column }
        require(seat.isNotEmpty()) { "해당 상영관에는 해당 좌석이 존재하지 않습니다." }
        require(seat.first().isReserved != SeatState.RESERVED) { "해당 좌석은 이미 예약되었습니다." }

        return seat.first()
    }
}
