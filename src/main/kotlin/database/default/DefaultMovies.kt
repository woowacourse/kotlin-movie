package database.default

import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object DefaultMovies {
    data class MovieRow(
        val id: Int,
        val name: String,
        val runningTimeMinutes: Int,
    )

    val rows: List<MovieRow> =
        listOf(
            MovieRow(
                id = 1,
                name = "인터스텔라",
                runningTimeMinutes = 169,
            ),
            MovieRow(
                id = 2,
                name = "오펜하이머",
                runningTimeMinutes = 180,
            ),
        )

    fun all(): List<Movie> = rows.map { it.toMovie() }

    private fun MovieRow.toMovie(): Movie =
        Movie(
            id = MovieId(Uuid.generateV7()),
            name = MovieName(name),
            runningTime = RunningTime(runningTimeMinutes),
        )
}
