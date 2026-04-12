package model.movie

import java.time.LocalDate

data class Movie(
    val title: String,
    val runningTime: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    init {
        require(startDate <= endDate) { "시작일이 종료일보다 클 수 없습니다" }
        require(runningTime > 0) { "상영 시간은 0보다 커야 합니다" }
    }
}
