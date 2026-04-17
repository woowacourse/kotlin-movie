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
import java.time.LocalDateTime

class MovieTest {
    @Test
    fun `영화 ID와 제목, 상영 목록을 갖고 있다`() {
        // given
        val id = 1L
        val screenings =
            Screenings(
                listOf(
                    Screening(
                        1L,
                        screen = Screen(1, SeatsData.seats),
                        screeningDateTime =
                            ScreeningDateTime(
                                LocalDateTime.of(2026, 4, 9, 10, 20),
                                LocalDateTime.of(2026, 4, 9, 13, 0),
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
