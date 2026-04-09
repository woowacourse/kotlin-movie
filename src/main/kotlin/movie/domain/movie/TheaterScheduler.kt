package movie.domain.movie

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TheaterScheduler(
    private val theaters: Theaters,
    private val screeningMovies: ScreeningMovies = ScreeningMovies(),
) {
    fun addMovie(
        theater: Theater,
        movie: Movie,
        movieTime: MovieTime,
    ) {
        if (checkDuplicateTime(theater = theater, movieTime = movieTime)) {
            throw IllegalArgumentException("시간 중복으로 영화를 추가할 수 없습니다.")
        }

        val newMovie = createScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = movieTime,
        )

        screeningMovies.addMovie(newMovie)
    }

    fun createScreeningMovie(theater: Theater, movie: Movie, movieTime: MovieTime): ScreeningMovie {
        return ScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = movieTime,
        )
    }

    fun checkDuplicateTime(
        theater: Theater,
        movieTime: MovieTime,
    ): Boolean {
       return screeningMovies.checkDuplicateTime(
           theater = theater,
           movieTime = movieTime,
       )
    }

    fun getMovieTitles(): List<MovieTitle> {
        return screeningMovies.getMovieTitles()
    }

    fun getMovieTimes(movie: Movie): List<MovieTime> {
        return screeningMovies.getMovieTimes(movie = movie)
    }
}
