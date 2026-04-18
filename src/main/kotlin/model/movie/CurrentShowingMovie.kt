package model.movie

class CurrentShowingMovie(
    movies: List<Movie>,
) {
    private val movies = movies.toList()

    fun findByName(name: String): Movie? = movies.firstOrNull { it.isSameName(MovieName(name)) }
}
