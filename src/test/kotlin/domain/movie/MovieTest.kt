package domain.movie

import domain.screening.Screenings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class MovieTest {
    @Test
    fun `영화 ID와 상영 목록을 갖고 있다`() {
        // given
        val id = UUID.randomUUID()
        val screenings = Screenings(emptyList())
        val movie = Movie(id, screenings)
        // when

        // then
        assertThat(id).isEqualTo(id)
        assertThat(movie.screenings).isEqualTo(screenings)
    }
}
