package domain.screening

import domain.ticket.TicketBucket
import domain.movie.Title
import java.time.LocalDate

data class ScreeningSchedule(
    val screenings: List<Screening>,
) {
    init {
        require(screenings.isNotEmpty()) { "상영 일정이 없습니다. 영화관 문 닫았습니다.. 망했음.." }
        val screeningsByMovie = screenings.groupBy { it.movie }

        screeningsByMovie.values.forEach { movieScreenings ->
            require(movieScreenings.size == movieScreenings.distinctBy { it.startTime }.size) {
                "동일한 영화의 상영 시간은 중복될 수 없습니다."
            }
        }
    }

    fun getMovieSchedule(
        title: Title,
        date: LocalDate,
    ): ScreeningSchedule = ScreeningSchedule(screenings.filter { it.movie.title == title && it.startTime.toLocalDate() == date })

    fun reserve(bucket: TicketBucket): ScreeningSchedule {

        val allScreeningIds = screenings.map { it.id }.toSet()
        require(bucket.tickets.all { it.screening.id in allScreeningIds }) {
            "존재하지 않는 상영입니다."
        }
        val updatedScreenings =
            screenings.map { screening ->
                val ticketsForScreening = bucket.tickets.filter { it.screening.id == screening.id }

                ticketsForScreening.fold(screening) { accScreening, ticket ->
                    ticket.seatPositions.positions.fold(accScreening) { acc, position ->
                        acc.reserve(position)
                    }
                }
            }
        return ScreeningSchedule(updatedScreenings)
    }
}
