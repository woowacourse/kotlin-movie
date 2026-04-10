package model.schedule

import model.movie.Movie
import model.seat.SeatInventory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ScreeningTest {
    @Test
    fun `일정은 선택한 좌석들을 예약할 수 있다`() {
        val screening = createScreening()
        val seatNames = listOf("A1", "B2")

        val reservedScreening = screening.reserveSeats(seatNames)

        seatNames.forEach { name ->
            assertThat(reservedScreening.seatInventory.findSeat(name).isReserved).isTrue
        }
    }

    @Test
    fun `두 일정의 시간이 겹치는지 알 수 있다`() {
        val screening1 = createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0))
        val screening2 = createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 11, 0))

        assertThat(screening1.isOverlapping(screening2)).isTrue
    }

    @Test
    fun `두 일정의 시간이 겹치지 않으면 겹치지 않음을 알 수 있다`() {
        val screening1 = createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 10, 0))
        val screening2 = createScreening(startDateTime = LocalDateTime.of(2026, 4, 10, 13, 0))

        assertThat(screening1.isOverlapping(screening2)).isFalse
    }

    private fun createScreening(startDateTime: LocalDateTime = LocalDateTime.of(2026, 4, 10, 10, 0)): Screening =
        Screening(
            movie =
                Movie(
                    movieTitle = "인터스텔라",
                    movieRunningTime = 130,
                    startDate = LocalDate.of(2026, 4, 1),
                    endDate = LocalDate.of(2026, 4, 30),
                ),
            startDateTime = startDateTime,
            seatInventory = SeatInventory.createDefaultSeatInventory(),
        )
}
