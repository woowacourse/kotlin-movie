package movie.domain.movie

import movie.domain.Price
import movie.domain.seat.number.SeatNumber
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ScreeningMovie(
    val theater: Theater,
    val movie: Movie,
    val movieTime: MovieTime,
    reservedSeats: List<SeatNumber> = emptyList()
) {
    private val _reservedSeats = reservedSeats.toMutableList()

    fun reserve(targetSeatNumbers: List<SeatNumber>) {
        targetSeatNumbers.forEach {
            reserveCheck(it)
        }

        _reservedSeats.addAll(targetSeatNumbers)
    }

    fun reserveCheck(seatNumber: SeatNumber) {
        require(theater.validateSeat(seatNumber)) { "존재하지 않는 좌석입니다." }

        if (_reservedSeats.contains(seatNumber)) {
            throw IllegalArgumentException("이미 예약된 좌석입니다.")
        }
    }

    fun calculatePrice(targetSeatNumbers: List<SeatNumber>): Price {
        return targetSeatNumbers
            .map { theater.getPrice(it) }
            .fold(Price(0)) { price, targetPrice ->
                price.sumPrice(targetPrice = targetPrice)
            }
    }

    fun getMovieTitle(): MovieTitle {
        return movie.title
    }

    fun getMovieId(): Uuid {
        return movie.id
    }
}
