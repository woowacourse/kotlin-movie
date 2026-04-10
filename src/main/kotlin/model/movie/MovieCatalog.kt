package model.movie

class MovieCatalog(
    movies: List<Movie>
) {
    private val movies = movies.toList()

    fun findByName(name: String): Movie? =
        movies.firstOrNull { it.name.name == name }
}