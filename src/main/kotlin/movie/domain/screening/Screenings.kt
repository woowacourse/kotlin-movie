package movie.domain.screening

import java.time.LocalDate

data class Screenings(
    private val screenings: List<Screening>,
) {
    fun hasScreeningOnDate(date: LocalDate): Boolean = screenings.any { it.occursOn(date) }

    fun getScreeningsByDate(date: LocalDate): Screenings = Screenings(screenings.filter { it.occursOn(date) })

    fun forEachIndexed(action: (Int, Screening) -> Unit) {
        screenings.forEachIndexed(action)
    }

    fun findByNumber(number: Int): Screening {
        require(number in 1..screenings.size) { "유효하지 않은 상영 번호입니다." }
        return screenings[number - 1]
    }

    fun toList(): List<Screening> = screenings
}
