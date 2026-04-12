package movie.domain.seat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatColumnTest {

    @Test
    fun `SeatColumn은 1에서 4 사이여야 한다`() {
        assertThrows<IllegalArgumentException> {
            SeatColumn(5)
        }
    }

    @Test
    fun `SeatColumn은 빈값일 수 없다`() {
        assertThrows<IllegalArgumentException> {
            SeatColumn(0)
        }
    }
}
