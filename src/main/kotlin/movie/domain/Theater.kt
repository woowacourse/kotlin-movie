package movie.domain

import movie.domain.seat.Seat
import movie.domain.seat.SeatNumber
import movie.domain.seat.SeatRank

class Theater {
    private val seats = createSeats()

    fun getSeats() = seats

    fun isValidSeatNumber(seatNumber: SeatNumber): Boolean {
        return seats.any {
            it.seatNumber == seatNumber
        }
    }

    fun getPrice(seatNumber: SeatNumber): Price {
        return seats.firstOrNull { it.seatNumber == seatNumber }
            ?.getPrice()
            ?: throw IllegalArgumentException ("존재하지 않는 좌석입니다.")
    }

    companion object {
        private fun createSeats(): List<Seat> {
            val seatNumbers = listOf(
                SeatNumber(row = 'A', col = 1),
                SeatNumber(row = 'A', col = 2),
                SeatNumber(row = 'A', col = 3),
                SeatNumber(row = 'A', col = 4),
                SeatNumber(row = 'B', col = 1),
                SeatNumber(row = 'B', col = 2),
                SeatNumber(row = 'B', col = 3),
                SeatNumber(row = 'B', col = 4),
                SeatNumber(row = 'C', col = 1),
                SeatNumber(row = 'C', col = 2),
                SeatNumber(row = 'C', col = 3),
                SeatNumber(row = 'C', col = 4),
                SeatNumber(row = 'D', col = 1),
                SeatNumber(row = 'D', col = 2),
                SeatNumber(row = 'D', col = 3),
                SeatNumber(row = 'D', col = 4),
                SeatNumber(row = 'E', col = 1),
                SeatNumber(row = 'E', col = 2),
                SeatNumber(row = 'E', col = 3),
                SeatNumber(row = 'E', col = 4),
            )

            return seatNumbers.map {
                val rank = when (it.row) {
                    in 'A'..'B' -> SeatRank.B
                    in 'C'..'D' -> SeatRank.S
                    else -> SeatRank.A
                }

                Seat(
                    seatNumber = it,
                    rank = rank,
                )
            }
        }
    }
}
