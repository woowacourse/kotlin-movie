package movie.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `좌석은 등급에 맞는 가격을 반환한다`() {
        val seatS = Seat(SeatNumber(row = 'A', col = 1), SeatRank.S)
        val seatA = Seat(SeatNumber(row = 'A', col = 1), SeatRank.A)
        val seatB = Seat(SeatNumber(row = 'A', col = 1), SeatRank.B)

        assertThat(seatS.getPrice()).isEqualTo(Price(18_000))
        assertThat(seatA.getPrice()).isEqualTo(Price(15_000))
        assertThat(seatB.getPrice()).isEqualTo(Price(12_000))
    }
}
