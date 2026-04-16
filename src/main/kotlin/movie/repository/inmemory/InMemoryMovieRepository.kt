package movie.repository.inmemory

import movie.model.movie.Movie
import movie.model.movie.Movies
import movie.model.movie.RunningTime
import movie.repository.MovieRepository

object InMemoryMovieRepository : MovieRepository {
    val F1_THE_MOVIE = Movie(title = "F1 더 무비", runningTime = RunningTime(160))
    val TOY_STORY = Movie(title = "토이 스토리", runningTime = RunningTime(150))
    val IRON_MAN = Movie(title = "아이언맨", runningTime = RunningTime(130))
    val ROBOT_DREAM = Movie(title = "로봇 드림", runningTime = RunningTime(110))

    private val movies = getMovies()

    override fun getMovies(): Movies = Movies(listOf(F1_THE_MOVIE, TOY_STORY, IRON_MAN, ROBOT_DREAM))

    override fun findById(id: Long): Movie? = movies.findById(id)

    override fun findByTitle(title: String): Movie? = movies.findByTitle(title)
}
