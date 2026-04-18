package database.repository

import database.Database
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieScreeningRepositoryTest {
    private val movieRepository = MovieRepository()
    private val screeningRepository = MovieScreeningRepository()

    @BeforeEach
    fun setUp() {
        val testUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
        Database.init(url = testUrl)
        movieRepository.save()
        screeningRepository.save()
    }

    @Test
    fun `존재하는 영화 이름으로 조회하면 상영 목록을 반환한다`() {
        val result = screeningRepository.findScreeningsByMovieName("인터스텔라")
        assertThat(result).isNotNull
        assertThat(result).isNotEmpty
    }

    @Test
    fun `존재하지 않는 영화 이름으로 조회하면 null을 반환한다`() {
        val result = screeningRepository.findScreeningsByMovieName("이영화는존재하지않아요")
        assertThat(result).isNull()
    }
}
