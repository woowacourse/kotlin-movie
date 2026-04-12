package movie.domain

import java.time.LocalDate
import java.time.LocalDateTime

class Schedules(
    schedules: List<Schedule>,
) {
    private val _schedules = schedules.toMutableList()

    fun addSchedule(schedule: Schedule) {
        _schedules.add(schedule)
    }

    fun getMovieSchedules(movieTitle: String, date: LocalDate): List<Schedule> {
        return _schedules.filter {
            it.movie.title == movieTitle && it.startTime.toLocalDate() == date
        }
    }

    fun getMovieTitles(): List<String> {
        return _schedules.map { it.movie.title }
            .distinct()
    }

    fun getSchedule(movieTitle: String, startTime: LocalDateTime): Schedule {
        return _schedules.firstOrNull {
            it.movie.title == movieTitle && it.startTime == startTime
        } ?: throw IllegalArgumentException("영화 제목 또는 시작 시간이 올바르지 않습니다.")
    }
}
