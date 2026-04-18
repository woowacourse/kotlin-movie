package movie.domain

import movie.error.MovieErrorMessage
import movie.error.ScheduleErrorMessage
import java.time.LocalDate
import java.time.LocalDateTime

class Schedules(
    schedules: List<Schedule>,
) {
    private val schedules = schedules.toMutableList()

    fun addSchedule(schedule: Schedule) {
        schedules.add(schedule)
    }

    fun getMovieSchedules(
        movieTitle: MovieTitle,
        date: LocalDate,
    ): List<Schedule> {
        val schedules =
            schedules.filter {
                it.movie.title == movieTitle && it.startTime.toLocalDate() == date
            }

        require(schedules.isNotEmpty()) { MovieErrorMessage.NOT_FOUND_ON_DATE }

        return schedules
    }

    fun getMovieTitles(): List<MovieTitle> =
        schedules
            .map { it.movie.title }
            .distinct()

    fun getSchedule(
        movieTitle: MovieTitle,
        startTime: LocalDateTime,
    ): Schedule =
        schedules.firstOrNull {
            it.movie.title == movieTitle && it.startTime == startTime
        } ?: throw IllegalArgumentException(ScheduleErrorMessage.INVALID_MOVIE_OR_TIME)

    fun getScheduleById(id: Long): Schedule =
        schedules.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException(ScheduleErrorMessage.SCREENING_NOT_FOUND)
}
