package model.movie

import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class MovieTest {
    private val movieOne =
        Movie(
            name = MovieName("혼자사는남자"),
            id = MovieId(Uuid.generateV7()),
            runningTime = RunningTime(10),
        )
    private val movieTwo =
        Movie(
            name = MovieName("혼자사는남자"),
            id = MovieId(Uuid.generateV7()),
            runningTime = RunningTime(10),
        )

    @Test
    fun `러닝 타임이 같아도 id가 다르면 다른 영화로 판단한다`() {
        assertThat(
            movieOne,
        ).isNotEqualTo(
            movieTwo,
        )
    }
}
