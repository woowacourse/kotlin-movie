package movie.domain.movie

class ScreeningMovies(
    screeningMovies: List<ScreeningMovie> = emptyList()
) {
    private val _screeningMovies = screeningMovies.toMutableList()

    fun addMovie(movie: ScreeningMovie) {
        _screeningMovies.add(movie)
    }

    fun checkDuplicateTime(
        theater: Theater,
        movieTime: MovieTime,
    ): Boolean {
        return _screeningMovies.filter {
            it.theater.sameTheater(theater)
        }.any {
            it.movieTime.checkDuplicate(movieTime)
        }
    }

    fun getMovieTitles(): List<MovieTitle> {
        return _screeningMovies
            .map { it.movie.title }
            .distinct()
    }

    fun getMovieTimes(movie: Movie): List<MovieTime> {
        return _screeningMovies
            .filter { it.movie.isSameMovie(movie) }
            .map { it.movieTime }
    }
}
