package movie.domain

import movie.MovieTitle
import java.time.LocalDate
import java.time.LocalDateTime

class Schedules(
    schedules: List<Schedule>,
) {
    private val _schedules = schedules.toMutableList()

    fun addSchedule(schedule: Schedule) {
        _schedules.add(schedule)
    }

    fun getMovieSchedules(movieTitle: MovieTitle, date: LocalDate): List<Schedule> {
        val schedules = _schedules.filter {
            it.movie.title == movieTitle && it.startTime.toLocalDate() == date
        }

        require(schedules.isNotEmpty()) { "해당 영화가 해당 날짜에 상영하지 않습니다" }

        return schedules
    }

    fun getMovieTitles(): List<MovieTitle> {
        return _schedules.map { it.movie.title }
            .distinct()
    }

    fun getSchedule(movieTitle: MovieTitle, startTime: LocalDateTime): Schedule {
        return _schedules.firstOrNull {
            it.movie.title == movieTitle && it.startTime == startTime
        } ?: throw IllegalArgumentException("영화 제목 또는 시작 시간이 올바르지 않습니다.")
    }
}
