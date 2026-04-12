package movie.domain.seat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatPositionsTest {
    @Test
    fun `좌석 위치 목록은 비어 있을 수 없다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                SeatPositions(emptyList())
            }

        assert(exception.message == "좌석 위치 목록은 비어 있을 수 없습니다.")
    }
}

