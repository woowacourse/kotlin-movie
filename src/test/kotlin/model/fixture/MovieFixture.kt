package model.fixture

import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object MovieFixture {
    fun create(
        name: MovieName = MovieName("혼자사는남자"),
        id: MovieId = MovieId(Uuid.generateV7()),
        runningTime: RunningTime = RunningTime(30),
    ) = Movie(
        name = name,
        id = id,
        runningTime = runningTime,
    )
}
