package movie.domain

import java.time.LocalDate

class Schedules(
    schedules: List<Schedule>,
) {
    private val _schedules = schedules.toMutableList()

    fun addSchedule(schedule: Schedule) {
        _schedules.add(schedule)
    }

    fun getMovieSchedule(movieTitle: String, date: LocalDate): List<Schedule> {
        return _schedules.filter {
            it.movie.title == movieTitle && it.startTime.toLocalDate() == date
        }
    }

    fun getMovieTitles(): List<String> {
        return _schedules.map { it.movie.title }
            .distinct()
    }
}
