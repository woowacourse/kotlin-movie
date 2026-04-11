package domain.cinema

import domain.reservation.ReservationInfos

class Showings(val showings: List<Showing>) {
    operator fun get(index: Int): Showing = showings[index]

    fun first(): Showing = showings.first()

    fun findByMovieAndDate(
        movie: Movie,
        movieTime: MovieTime,
    ): Showings {
        val filtered = showings.filter { it.movie == movie && it.startTime.isOnSameDate(movieTime) }
        require(filtered.isNotEmpty()) { "해당 영화는 해당 날짜에 상영되지 않습니다." }
        return Showings(filtered)
    }

    fun findByIndex(input: String): Showing {
        require(input.toIntOrNull() != null && input.toInt() <= showings.size) { "선택하신 상영 번호는 없는 상영 번호입니다." }
        return showings[input.toInt() - 1]
    }

    fun findAvailableShowing(
        movie: Movie,
        movieTime: MovieTime,
        number: String,
        reservationInfos: ReservationInfos,
    ): Showing {
        val showings = findByMovieAndDate(movie, movieTime)
        val selected = showings.findByIndex(number)
        reservationInfos.checkReservationHistory(selected)

        return selected
    }
}
