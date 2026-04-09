package movie.domain.movie

import movie.domain.Price

class Reservations(
    reservations: List<Reservation> = emptyList(),
) {
    private val value = reservations.toMutableList()

    fun getReservations() = value.toList()

    fun addReservation(reservation: Reservation) {
        value.add(reservation)
    }

    fun getTotalPrice(): Price =
        value
            .map { it.getTotalPrice() }
            .fold(Price(0)) { total, price ->
                total.sumPrice(price)
            }

    fun isDupTime(movieTime: MovieTime): Boolean =
        value.any {
            val srcMovieTime = it.screeningMovie.movieTime
            srcMovieTime.date == movieTime.date &&
                srcMovieTime.startTime in movieTime.startTime..movieTime.endTime &&
                srcMovieTime.endTime in movieTime.startTime..movieTime.endTime
        }
}
