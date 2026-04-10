package movie.domain.movie

import movie.domain.screening.Screen
import movie.domain.screening.ScreenId
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.screening.Screenings
import movie.domain.seat.ReservatedSeats
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
                        title = MovieTitle("F1 더 무비"),
                        screen = Screen(ScreenId(1), Seats.createDefault()),
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
        val movie = Movie(id, MovieTitle("F1 더 무비"), screenings)

        // then
        assertThat(id).isEqualTo(id)
        assertThat(movie.screenings).isEqualTo(screenings)
    }
}
