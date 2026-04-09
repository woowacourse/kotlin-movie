package model.schedule

import model.movie.MovieName

class MovieNameGroup(
    movieNames: List<MovieName>,
) {
    private val movieNames = movieNames.toList()

    fun find(name: String): MovieName? = movieNames.find { it.name == name }

    fun contains(movieName: MovieName): Boolean = movieName in movieNames
}

class CinemaSchedule(
    screenSchedules: List<ScreenSchedule>,
) {
    private val screenSchedules = screenSchedules.toList()

    init {
        require(screenSchedules.distinct().size == screenSchedules.size)
    }

    fun getMovieSchedule(movie: MovieName): MovieSchedule = MovieSchedule(screenSchedules.flatMap { it.getMovieSchedule(movie) })
}
