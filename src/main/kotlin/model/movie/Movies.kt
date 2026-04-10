package model.movie

import java.time.LocalDate

class Movies(
    val value: List<Movie>,
) {
    init {
        require(value.isNotEmpty()) { "영화 정보가 없습니다" }
    }

    fun findMovie(movieTitle: String): Movie =
        value.find { it.movieTitle == movieTitle }
            ?: throw IllegalArgumentException("존재하지 않는 영화입니다")

    companion object {
        fun createMovies(): Movies =
            Movies(
                value =
                    listOf(
                        Movie(
                            movieTitle = "탑건: 매버릭",
                            movieRunningTime = 130,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "마더",
                            movieRunningTime = 100,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "스파이더맨: 노 웨이 홈",
                            movieRunningTime = 140,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "남은 인생 10년",
                            movieRunningTime = 120,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "아이언맨 3",
                            movieRunningTime = 120,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "오늘 밤 이세상에서 사랑이 사라진다 해도",
                            movieRunningTime = 100,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "체인소맨",
                            movieRunningTime = 100,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                        Movie(
                            movieTitle = "호퍼스",
                            movieRunningTime = 100,
                            startDate = LocalDate.of(2026, 4, 1),
                            endDate = LocalDate.of(2026, 4, 30),
                        ),
                    ),
            )
    }
}
