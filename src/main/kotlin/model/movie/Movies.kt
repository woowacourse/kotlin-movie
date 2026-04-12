package model.movie

class Movies(
    val value: List<Movie>,
) {
    init {
        require(value.isNotEmpty()) { "영화 정보가 없습니다" }
    }

    fun findMovie(movieTitle: String): Movie =
        value.find { it.movieTitle == movieTitle }
            ?: throw IllegalArgumentException("존재하지 않는 영화입니다")
}
