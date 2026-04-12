package movie.domain.movie

import movie.data.SeatsData
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservedSeats
import movie.domain.seat.Seats
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
                        screen = Screen(1, SeatsData.seats),
                        screeningDateTime =
                            ScreeningDateTime(
                                LocalDate.of(2026, 4, 9),
                                LocalTime.of(10, 20),
                                LocalTime.of(13, 0),
                            ),
                        reservedSeats = ReservedSeats(Seats(emptySet())),
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
