package model.screening

import model.Screen
import model.seat.SeatNumber
import model.seat.Seats

class ScreeningSeatMap(
    val screen: Screen,
    private val reservedSeats: Seats = Seats(emptyList()),
) {
    fun reserve(seatNumbers: List<SeatNumber>): ScreeningSeatMap {
        require(seatNumbers.distinct().size == seatNumbers.size) { "중복된 좌석은 선택할 수 없습니다." }
        seatNumbers.forEach {
            require(!reservedSeats.seatNumbers.contains(it)) { "이미 예약된 좌석입니다." }
        }
        val newReservedSeats = Seats(seatNumbers.map { screen.seats.findSeat(it) })
        val updatedSeats = reservedSeats.addSeats(newReservedSeats)
        return ScreeningSeatMap(screen, updatedSeats)
    }

    fun getAvailableSeats(): Seats = screen.seats.excludeReserved(reservedSeats)
}
