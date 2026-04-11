package model.schedule

import model.movie.MovieName

class MovieNameGroup(
    movieNames: List<MovieName>,
) {
    private val movieNames = movieNames.toList()

    fun find(name: String): MovieName? = movieNames.find { it.name == name }

    fun contains(movieName: MovieName): Boolean = movieName in movieNames
}
