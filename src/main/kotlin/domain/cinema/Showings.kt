package domain.cinema

import kotlinx.datetime.LocalDate

class Showings(val showings: List<Showing>) {
    val size: Int get() = showings.size

    operator fun get(index: Int): Showing = showings[index]

    fun first(): Showing = showings.first()

    fun findByMovieAndDate(
        movie: Movie,
        date: LocalDate,
    ): Showings {
        val filtered = showings.filter { it.movie == movie && it.startTime.date == date }
        require(filtered.isNotEmpty()) { "해당 영화는 해당 날짜에 상영되지 않습니다." }
        return Showings(filtered)
    }
}
