package database.default

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DefaultMoviesTest {
    @Test
    fun `상영중인 영화 목록은 비어있지 않다`() {
        assertThat(DefaultMovies.all()).isNotEmpty
    }
}
