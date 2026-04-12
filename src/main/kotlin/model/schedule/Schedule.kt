package model.schedule

import model.movie.Movie
import java.time.LocalTime

class Schedule(
    val openTime: LocalTime,
    val closeTime: LocalTime,
    val screenings: List<Screening>,
) {
    init {
        require(openTime < closeTime) { "시작 시간이 종료 시간보다 클 수 없습니다" }
    }

    // 변경된 Screening에 대한 Schedule 변경 함수
    fun updateScreening(
        old: Screening,
        new: Screening,
    ): Schedule =
        Schedule(
            openTime = openTime,
            closeTime = closeTime,
            screenings =
                screenings.map {
                    if (it == old) new else it
                },
        )

    // 제목과 일치하는 영화의 상영 목록을 반환하는 함수
    fun getScreeningsByMovie(movie: Movie): List<Screening> = screenings.filter { it.movie == movie }
}
