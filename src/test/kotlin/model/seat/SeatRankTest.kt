package model.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SeatRankTest {
    @Test
    fun `각 등급은 12_000, 15_000, 18_000원의 가격을 갖는다`() {
        val seatSRank = SeatRank.S_RANK
        val seatARank = SeatRank.A_RANK
        val seatBRank = SeatRank.B_RANK

        assertThat(seatSRank.price.value).isEqualTo(18000)
        assertThat(seatARank.price.value).isEqualTo(15000)
        assertThat(seatBRank.price.value).isEqualTo(12000)
    }
}
