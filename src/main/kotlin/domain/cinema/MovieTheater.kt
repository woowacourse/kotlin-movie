package domain.cinema

import domain.Id
import domain.reservation.Cart
import kotlinx.datetime.LocalDate
import util.ErrorMessage

class MovieTheater(
    val screens: List<Screen>,
    val movies: List<Movie>,
    val screenings: List<ScreeningSchedule>,
) {
    fun findMovie(title: String): Movie? = movies.find { it.title == title }

    fun findMovieById(id: Id): Movie? = movies.find { it.id.value == id.value }

    fun chooseMovie(title: String): Movie = requireNotNull(findMovie(title)) { ErrorMessage.MOVIE_NOT_FOUND }

    fun findScreenings(
        movie: Movie,
        date: LocalDate,
    ): List<ScreeningSchedule> = screenings.filter { it.movie == movie && it.startTime.date == date }

    fun validateScreeningDate(
        movie: Movie,
        input: String,
    ): LocalDate {
        val date = runCatching { LocalDate.parse(input) }.getOrNull()
        require(date != null) { ErrorMessage.INVALID_DATE_FORMAT }
        require(findScreenings(movie, date).isNotEmpty()) { ErrorMessage.SCREENING_NOT_FOUND_FOR_DATE }

        return date
    }

    fun chooseScreening(
        cart: Cart,
        movie: Movie,
        date: LocalDate,
        input: String,
    ): ScreeningSchedule {
        val screenings = findScreenings(movie, date)
        val screeningNumber = input.toIntOrNull()

        require(screeningNumber != null && screeningNumber in 1..screenings.size) { ErrorMessage.INVALID_SCREENING_NUMBER }

        return screenings[screeningNumber - 1].also(cart::checkReservationHistory)
    }
}
