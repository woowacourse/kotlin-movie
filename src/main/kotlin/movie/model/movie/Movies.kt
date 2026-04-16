package movie.model.movie

class Movies(
    movies: List<Movie>,
) : Iterable<Movie> {
    private val value = movies.toList()

    override fun iterator(): Iterator<Movie> = value.iterator()

    init {
        require(this.value.isNotEmpty()) { "영화 목록이 비어있으면 안됩니다." }
    }

    fun isInclude(movie: Movie): Boolean = value.contains(movie)

    fun findByTitle(title: String): Movie? = value.find { it.title == title }

    fun findById(id: Long): Movie? = value.find { it.id == id }
}
