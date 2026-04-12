package movie.domain.movie

class Movies(
    private val movies: List<Movie>,
) {
    fun findMovie(title: String): Movie = movies.find { it.title == title } ?: throw IllegalArgumentException("존재하지 않는 영화입니다.")
}
