package repository

import model.movie.Movies

interface MovieRepository {
    fun findAll(): Movies

    fun findIdByTitle(title: String): Long?
}
