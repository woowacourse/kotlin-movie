package movie.domain.movie

class Movies(
    private val movies: List<Movie>,
) {
    fun findMovie(title: String): Movie = movies.find { it.title == title } ?: throw IllegalArgumentException("영화를 찾을 수 없습니다.")
}
