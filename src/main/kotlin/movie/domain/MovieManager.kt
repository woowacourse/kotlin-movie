package movie.domain

import java.time.LocalDate
import java.time.LocalDateTime

class MovieManager(
    val schedules: Schedules,
) {

    fun getMovieStartTime(movieTitle: String, date: LocalDate): List<LocalDateTime> {
        return schedules.getMovieSchedules(
            movieTitle = movieTitle,
            date = date
        ).map { it.startTime }
    }

    fun getMovieTitle(): List<String> = schedules.getMovieTitles()

    fun hasMovieTitle(title: String): Boolean {
        return getMovieTitle().contains(title)

    }

    fun getSchedule(title: String, startTime: LocalDateTime): Schedule {
        return schedules.getSchedule(movieTitle = title, startTime = startTime)
    }
}
