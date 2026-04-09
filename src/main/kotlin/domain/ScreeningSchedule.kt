package domain

import domain.model.Screening

data class ScreeningSchedule(val screenings: List<Screening>) {

    init {
        val screeningsByMovie = screenings.groupBy { it.movie }

        screeningsByMovie.values.forEach { movieScreenings ->
            require(movieScreenings.size == movieScreenings.distinctBy { it.startTime }.size) {
                "동일한 영화의 상영 시간은 중복될 수 없습니다."
            }
        }
    }
}
