package movie.domain

import java.time.LocalDate
import java.time.LocalDateTime

class MovieManager(
    val schedules: Schedules,
) {

    fun getMovieStartTime(movieTitle: String, date: LocalDate): List<LocalDateTime> {
        return schedules.getMovieSchedule(
            movieTitle = movieTitle,
            date = date
        ).map { it.startTime }
    }

    fun getMovieTitle(): List<String> = schedules.getMovieTitles()
}
