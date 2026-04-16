package movie.repository

import movie.model.movie.Movie
import movie.model.movie.Movies

interface MovieRepository {
    fun getMovies(): Movies

    fun findById(id: Long): Movie?

    fun findByTitle(title: String): Movie?
}
