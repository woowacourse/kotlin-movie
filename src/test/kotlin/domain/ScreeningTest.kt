package domain

import domain.fixture.createMovie
import domain.fixture.createScreening
import domain.fixture.createScreeningRoom
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime

class ScreeningTest {
    @Test
    fun `영화, 상영관, 상영 날짜 및 시간을 가진다`() {
        Screening(
            movie = createMovie(),
            room = createScreeningRoom(),
            startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
        )
    }

    @Test
    fun `상영 시작 시간과 영화 상영 길이에 따른 시작 및 종료 시간을 가진다`() {
        val screening =
            Screening(
                movie = createMovie(runningTime = RunningTime(167)),
                room = createScreeningRoom(),
                startTime = LocalDateTime.of(2026, 4, 8, 10, 0),
            )
        assertThat(screening.screenTimeRange.start).isEqualTo(LocalTime.of(10, 0))
        assertThat(screening.screenTimeRange.end).isEqualTo(LocalTime.of(12, 47))
    }

    @Test
    fun `상영 시간이 상영관의 운영시간에 포함되지 않을 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Screening(
                movie = createMovie(runningTime = RunningTime(167)),
                room =
                    createScreeningRoom(
                        openAt = LocalTime.of(10, 0),
                        closeAt = LocalTime.of(18, 0),
                    ),
                startTime = LocalDateTime.of(2026, 4, 8, 9, 0),
            )
        }
    }

    @Test
    fun `좌석을 예약 시 좌석의 상태가 바뀐다`() {
        val screening = createScreening()
        val position = SeatPosition(Row("A"), Column(1))
        val result =
            screening.reserve(positions = SeatPositions(listOf(position)))

        assertThat(
            result.seats.seats
                .first {
                    it.position == position
                }.state,
        ).isEqualTo(
            ReserveState.RESERVED,
        )
    }
}
