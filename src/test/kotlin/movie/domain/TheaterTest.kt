package movie.domain

import movie.domain.seat.SeatNumber
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TheaterTest {
    @Test
    fun `시트 넘버가 유효하다면 true를 반환한다`() {
        val theater = Theater()
        val seatNumber = SeatNumber(row = 'A', col = 1)

        assertThat(theater.isValidSeatNumber(seatNumber)).isTrue
    }

    @Test
    fun `시트 넘버가 유효하지 않다면 false를 반환한다`() {
        val theater = Theater()
        val seatNumber = SeatNumber(row = 'Z', col = 1)

        assertThat(theater.isValidSeatNumber(seatNumber)).isFalse
    }
}
