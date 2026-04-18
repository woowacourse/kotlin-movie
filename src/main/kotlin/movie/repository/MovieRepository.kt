package movie.repository

import movie.domain.movie.Movies

interface MovieRepository {
    fun findAll(): Movies
}
