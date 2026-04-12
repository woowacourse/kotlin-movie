package movie.domain

import movie.domain.MovieTitle
import java.time.LocalDate
import java.time.LocalDateTime

class MovieManager(
    val schedules: Schedules,
) {

    fun getMovieStartTime(movieTitle: MovieTitle, date: LocalDate): List<LocalDateTime> {
        return schedules.getMovieSchedules(
            movieTitle = movieTitle,
            date = date
        ).map { it.startTime }
    }

    fun getMovieTitle(): List<MovieTitle> = schedules.getMovieTitles()

    fun hasMovieTitle(title: MovieTitle): Boolean {
        return getMovieTitle().contains(title)

    }

    fun getSchedule(title: MovieTitle, startTime: LocalDateTime): Schedule {
        return schedules.getSchedule(movieTitle = title, startTime = startTime)
    }
}
