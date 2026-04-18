package model.schedule

import model.CinemaTime
import model.CinemaTimeRange
import model.fixture.MovieFixture
import model.movie.RunningTime
import model.reservation.MovieReservationResult
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class MovieScreeningTest {
    private val movie = MovieFixture.create(runningTime = RunningTime(60))

    @Test
    fun `영화, 상영관, 기간이 같은 일정은 동일한 일정으로 판단한다`() {
        val screenTime =
            CinemaTimeRange(
                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 12, 0)),
                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 13, 0)),
            )
        val seatGroup = SeatGroup(emptyList())
        assertThat(
            MovieScreening(
                movie = movie,
                screenTime = screenTime,
                seatGroup = seatGroup,
            ),
        ).isEqualTo(
            MovieScreening(
                movie = movie,
                screenTime = screenTime,
                seatGroup = seatGroup,
            ),
        )
    }

    @Test
    fun `배정된 시간의 길이가 영화의 러닝타임과 일치하지 않으면 에외를 발생시킨다`() {
        val screenTime =
            CinemaTimeRange(
                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 0)),
                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 10)),
            )

        assertThatCode {
            MovieScreening(
                movie = movie,
                screenTime = screenTime,
                seatGroup = SeatGroup(emptyList()),
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `배정된 시간의 길이가 영화의 러닝타임과 일치하면 예외를 발생시키지 않는다`() {
        val screenTime =
            CinemaTimeRange(
                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 0)),
                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 17, 0)),
            )
        assertThatCode {
            MovieScreening(
                movie = movie,
                screenTime = screenTime,
                seatGroup = SeatGroup(emptyList()),
            )
        }.doesNotThrowAnyException()
    }

    @Test
    fun `예약되지 않은 좌석을 예약하면 예약할 수 있다`() {
        val seat = Seat(row = SeatRow("A"), column = SeatColumn(1), grade = SeatGrade.S)
        val screenTime =
            CinemaTimeRange(
                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 0)),
                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 17, 0)),
            )
        assertThat(
            MovieScreening(
                movie = movie,
                screenTime =
                screenTime,
                seatGroup =
                    SeatGroup(
                        listOf(seat),
                    ),
            ).reserve(SeatRow("A"), SeatColumn(1)),
        ).isEqualTo(MovieReservationResult.Success(movie, screenTime, seat))
    }

    @Test
    fun `이미 예약된 좌석을 예약하면 예약할 수 없다`() {
        val seat = Seat(row = SeatRow("A"), column = SeatColumn(1), grade = SeatGrade.S)
        val movieScreening =
            MovieScreening(
                movie = movie,
                screenTime =
                    CinemaTimeRange(
                        start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 0)),
                        end = CinemaTime(LocalDateTime.of(2026, 4, 8, 17, 0)),
                    ),
                seatGroup =
                    SeatGroup(
                        listOf(seat),
                    ),
            )
        movieScreening.reserve(SeatRow("A"), SeatColumn(1))
        assertThat(
            movieScreening.reserve(SeatRow("A"), SeatColumn(1)),
        ).isEqualTo(MovieReservationResult.Failed)
    }

    @Test
    fun `이미 예약된 좌석을 취소하고 예약하면 예약할 수 있다 `() {
        val seat = Seat(row = SeatRow("A"), column = SeatColumn(1), grade = SeatGrade.S)
        val screenTime =
            CinemaTimeRange(
                start = CinemaTime(LocalDateTime.of(2026, 4, 8, 16, 0)),
                end = CinemaTime(LocalDateTime.of(2026, 4, 8, 17, 0)),
            )
        val movieScreening =
            MovieScreening(
                movie = movie,
                screenTime = screenTime,
                seatGroup =
                    SeatGroup(
                        listOf(seat),
                    ),
            )
        movieScreening.reserve(SeatRow("A"), SeatColumn(1))
        movieScreening.cancel(SeatRow("A"), SeatColumn(1))
        assertThat(
            movieScreening.reserve(SeatRow("A"), SeatColumn(1)),
        ).isEqualTo(
            MovieReservationResult.Success(
                movie,
                screenTime,
                seat,
            ),
        )
    }
}
