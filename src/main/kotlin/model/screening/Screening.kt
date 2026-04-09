package model.screening

import model.Screen
import model.movie.Movie
import model.reservation.Reservation
import model.seat.SeatNumber
import model.seat.Seats
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Screening(
    val movie: Movie,
    val startDateTime: LocalDateTime,
    val screen: Screen
) {
    private val reservedSeatNumbers: MutableSet<SeatNumber> = mutableSetOf()

    val endDateTime: LocalDateTime
        get() = startDateTime.plusMinutes(movie.runningTime.minute)

    val showDate: LocalDate
        get() = startDateTime.toLocalDate()

    val startShowTime: LocalTime
        get() = startDateTime.toLocalTime()

    fun reserve(seatNumbers: List<SeatNumber>): Reservation {
        require(seatNumbers.distinct().size == seatNumbers.size) { "중복된 좌석은 선택할 수 없습니다." }
        seatNumbers.forEach {
            require(!reservedSeatNumbers.contains(it)) { "이미 예약된 좌석입니다." }
        }
        val selectedSeats = seatNumbers.map { screen.seats.findSeat(it) }
        reservedSeatNumbers.addAll(seatNumbers)
        return Reservation(this, Seats(selectedSeats))
    }

    fun availableSeats(): Seats = screen.seats.excludeReserved(reservedSeatNumbers)

    fun isOverlapping(other: Screening): Boolean =
        startDateTime < other.endDateTime && endDateTime > other.startDateTime
}
