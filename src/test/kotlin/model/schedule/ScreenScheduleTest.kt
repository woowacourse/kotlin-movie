package model.schedule

import model.CinemaTime
import model.CinemaTimeRange
import model.fixture.MovieFixture
import model.movie.Movie
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import model.seat.SeatGroup
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ScreenScheduleTest {
    private val screenId1 = "1"
    private val screenId2 = "2"

    @Test
    fun `운영 기간이 같더라도 상영관 id가 다르면 다른 상영관 일정으로 판단한다`() {
        val cinemaTimeRange =
            CinemaTimeRange(
                CinemaTime(LocalDateTime.of(2026, 4, 7, 21, 50)),
                CinemaTime(LocalDateTime.of(2026, 4, 7, 22, 50)),
            )
        Assertions
            .assertThat(
                ScreenSchedule(
                    screenId = screenId1,
                    servicePeriod = cinemaTimeRange,
                    movieScreenings = emptyList(),
                ),
            ).isNotEqualTo(
                ScreenSchedule(
                    screenId = screenId2,
                    servicePeriod = cinemaTimeRange,
                    movieScreenings = emptyList(),
                ),
            )
    }

    @Test
    fun `운영 기간이 다르더라도 상영관 id가 같으면 같은 상영관 일정으로 판단한다`() {
        Assertions
            .assertThat(
                ScreenSchedule(
                    screenId = screenId1,
                    servicePeriod =
                        CinemaTimeRange(
                            CinemaTime(LocalDateTime.of(2026, 4, 8, 21, 50)),
                            CinemaTime(LocalDateTime.of(2026, 4, 8, 22, 50)),
                        ),
                    movieScreenings = emptyList(),
                ),
            ).isEqualTo(
                ScreenSchedule(
                    screenId = screenId1,
                    servicePeriod =
                        CinemaTimeRange(
                            CinemaTime(LocalDateTime.of(1999, 4, 8, 21, 50)),
                            CinemaTime(LocalDateTime.of(2026, 4, 8, 22, 50)),
                        ),
                    movieScreenings = emptyList(),
                ),
            )
    }

    @Test
    fun `상영관의 운영 시작 시간 전에  영화 상영 일정이 배정되면 예외를 발생시킨다`() {
        assertThatCode {
            ScreenSchedule(
                screenId = screenId1,
                servicePeriod =
                    CinemaTimeRange(
                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 7, 0)),
                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 8, 0)),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName("혼자사는남자"),
                                    id = MovieId(Uuid.generateV7()),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 6, 0)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 7, 0)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `상영관의 운영 종료 후 영화 상영 일정이 배정되면 예외를 발생시킨다`() {
        assertThatCode {
            ScreenSchedule(
                screenId = screenId1,
                servicePeriod =
                    CinemaTimeRange(
                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 7, 0)),
                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 8, 0)),
                    ),
                movieScreenings =
                    listOf(
                        MovieScreening(
                            movie =
                                Movie(
                                    name = MovieName("혼자사는남자"),
                                    id = MovieId(Uuid.generateV7()),
                                    runningTime = RunningTime(minute = 60),
                                ),
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 7, 30)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 8, 30)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                    ),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `특정 영화를 요청받으면 해당하는 상영관의 영화 전체 일정을 반환한다`() {
        val movie = MovieFixture.create(runningTime = RunningTime(minute = 60))
        val movieScreenings =
            listOf(
                MovieScreening(
                    movie = movie,
                    screenTime =
                        CinemaTimeRange(
                            start = CinemaTime(LocalDateTime.of(2026, 4, 7, 6, 0)),
                            end = CinemaTime(LocalDateTime.of(2026, 4, 7, 7, 0)),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
                MovieScreening(
                    movie = movie,
                    screenTime =
                        CinemaTimeRange(
                            start = CinemaTime(LocalDateTime.of(2026, 4, 7, 7, 30)),
                            end = CinemaTime(LocalDateTime.of(2026, 4, 7, 8, 30)),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
                MovieScreening(
                    movie = movie,
                    screenTime =
                        CinemaTimeRange(
                            start = CinemaTime(LocalDateTime.of(2026, 4, 7, 9, 0)),
                            end = CinemaTime(LocalDateTime.of(2026, 4, 7, 10, 0)),
                        ),
                    seatGroup = SeatGroup(emptyList()),
                ),
            )

        assertThat(
            ScreenSchedule(
                screenId = screenId1,
                servicePeriod =
                    CinemaTimeRange(
                        CinemaTime(LocalDateTime.of(2026, 4, 7, 6, 0)),
                        CinemaTime(LocalDateTime.of(2026, 4, 7, 22, 0)),
                    ),
                movieScreenings = movieScreenings,
            ).screeningOf(movie.id),
        ).isEqualTo(movieScreenings)
    }

    @ParameterizedTest
    @MethodSource("invalidMovieScreening")
    fun `동일한 상영관에서 상영 시간이 겹칠 경우 예외를 발생시킨다`(movieScreening: MovieScreening) {
        Assertions.assertThatThrownBy {
            ScreenSchedule(
                screenId = screenId1,
                servicePeriod =
                    CinemaTimeRange(
                        CinemaTime(LocalDateTime.of(1999, 4, 7, 21, 50)),
                        CinemaTime(LocalDateTime.of(2026, 4, 10, 22, 50)),
                    ),
                movieScreenings =
                    listOf(
                        movieScreening,
                        MovieScreening(
                            movie = movie,
                            screenTime =
                                CinemaTimeRange(
                                    start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                                    end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 0)),
                                ),
                            seatGroup = SeatGroup(emptyList()),
                        ),
                    ),
            )
        }
    }

    companion object {
        private val movie = MovieFixture.create(runningTime = RunningTime(minute = 60))

        @JvmStatic
        fun invalidMovieScreening(): List<Arguments> =
            listOf(
                Arguments.of(
                    MovieScreening(
                        movie = movie,
                        screenTime =
                            CinemaTimeRange(
                                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 10, 0)),
                                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 0)),
                            ),
                        seatGroup = SeatGroup(emptyList()),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movie,
                        screenTime =
                            CinemaTimeRange(
                                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 0)),
                                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 13, 0)),
                            ),
                        seatGroup = SeatGroup(emptyList()),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movie,
                        screenTime =
                            CinemaTimeRange(
                                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 30)),
                                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 30)),
                            ),
                        seatGroup = SeatGroup(emptyList()),
                    ),
                ),
                Arguments.of(
                    MovieScreening(
                        movie = movie,
                        screenTime =
                            CinemaTimeRange(
                                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 10, 30)),
                                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 11, 30)),
                            ),
                        seatGroup = SeatGroup(emptyList()),
                    ),
                ),
            )
    }
}
