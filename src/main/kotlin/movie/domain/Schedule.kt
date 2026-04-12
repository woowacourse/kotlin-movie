package movie.domain

import movie.domain.seat.SeatNumber
import movie.domain.seat.SelectedSeats
import java.time.LocalDateTime

class Schedule(
    val movie: Movie,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val selectedSeat: SelectedSeats = SelectedSeats(),
    val theater: Theater = Theater(),
) {
    init {
        require(startTime < endTime) { "영화 시작 시각은 종료 시각보다 빨라야 합니다" }
    }

    fun isReservationSeats(seatNumbers: List<SeatNumber>): Boolean {
        return seatNumbers.any { selectedSeat.isReservationSeat(it) }
    }
    fun isReservationSeat(seatNumber: SeatNumber): Boolean {
        return selectedSeat.isReservationSeat(seatNumber)
    }

    fun isDuplicateTime(target: Schedule): Boolean {
        return target.endTime in startTime..endTime
                || target.startTime in startTime..endTime
    }

    fun addSeats(seats: List<SeatNumber>) {
        require(seats.all { theater.isValidSeatNumber(it) }) { "유효하지 않은 좌석입니다." }

        selectedSeat.addSelectedSeats(seats)
    }

    fun getPrice(seatNumber: SeatNumber): Price {
        return theater.getPrice(seatNumber)
    }
}
