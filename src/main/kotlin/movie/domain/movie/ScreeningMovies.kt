package movie.domain.movie

class ScreeningMovies(
    screeningMovies: List<ScreeningMovie> = emptyList(),
) {
    private val value = screeningMovies.toMutableList()

    fun addMovie(movie: ScreeningMovie) {
        value.add(movie)
    }

    fun checkDuplicateTime(
        theater: Theater,
        movieTime: MovieTime,
    ): Boolean =
        value
            .filter {
                it.theater.sameTheater(theater)
            }.any {
                it.movieTime.checkDuplicate(movieTime)
            }

    fun getMovieTitles(): List<MovieTitle> =
        value
            .map { it.movie.title }
            .distinct()

    fun getMovieTimes(movie: Movie): List<MovieTime> =
        value
            .filter { it.movie.isSameMovie(movie) }
            .map { it.movieTime }
}
