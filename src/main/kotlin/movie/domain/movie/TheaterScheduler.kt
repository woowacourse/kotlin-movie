package movie.domain.movie

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TheaterScheduler(
    private val theaters: List<Theater>,
    screeningMovies: List<ScreeningMovie> = emptyList(),
) {
    private val _screeningMovies = screeningMovies.toMutableList()

    fun addMovie(
        theaterId: Uuid,
        movie: Movie,
        movieTime: MovieTime,
    ) {
        if (checkDuplicateTime(theaterId = theaterId, movieTime = movieTime)) {
            throw IllegalArgumentException("시간 중복으로 영화를 추가할 수 없습니다.")
        }

        val newMovie = createScreeningMovie(
            theater = getTheaterById(theaterId = theaterId),
            movie = movie,
            movieTime = movieTime,
        )

        _screeningMovies.add(newMovie)
    }

    private fun getTheaterById(theaterId: Uuid): Theater {
        return theaters.firstOrNull { it.id == theaterId }
            ?: throw IllegalArgumentException("존재하지 않는 상영관입니다.")
    }

    fun createScreeningMovie(theater: Theater, movie: Movie, movieTime: MovieTime): ScreeningMovie {
        return ScreeningMovie(
            theater = theater,
            movie = movie,
            movieTime = movieTime,
        )
    }

    fun checkDuplicateTime(
        theaterId: Uuid,
        movieTime: MovieTime,
    ): Boolean {
        return _screeningMovies.filter {
            it.theater.id == theaterId
        }.any {
            it.movieTime.checkDuplicate(movieTime)
        }
    }

    fun getMovieTitles(): List<MovieTitle> {
        return _screeningMovies
            .distinctBy { it.movie.id }
            .map { it.movie.title }
    }

    fun getMovieTimes(movieId: Uuid): List<MovieTime> {
        return _screeningMovies
            .filter { it.getMovieId() == movieId }
            .map { it.movieTime }
    }
}
