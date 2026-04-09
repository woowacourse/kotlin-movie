package domain.movie

import domain.screening.Screen
import domain.screening.Screening
import domain.screening.ScreeningDateTime
import domain.screening.Screenings
import domain.seat.ReservatedSeats
import domain.seat.Seats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class MovieTest {
    @Test
    fun `영화 ID와 제목, 상영 목록을 갖고 있다`() {
        // given
        val id = UUID.randomUUID()
        val screenings =
            Screenings(
                listOf(
                    Screening(
                        id = 1,
                        screen = Screen(1, Seats.createDefault()),
                        screeningDateTime =
                            ScreeningDateTime(
                                LocalDate.of(2026, 4, 9),
                                LocalTime.of(10, 20),
                                LocalTime.of(13, 0),
                            ),
                        reservatedSeats = ReservatedSeats(listOf()),
                    ),
                ),
            )

        // when
        val movie = Movie(id, "F1 더 무비", screenings)

        // then
        assertThat(id).isEqualTo(id)
        assertThat(movie.screenings).isEqualTo(screenings)
    }
}
