package movie.domain

import movie.domain.MovieTitle

class Movie(
    val title: MovieTitle,
    val runningTime: Int,
) {
    init {
        require(runningTime > 0) { "영화 러닝 타임은 0분 이하일 수 없습니다." }
    }
}
