package movie.domain.seat

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatRowTest {

    @Test
    fun `SeatRow는 A에서 E 사이여야 한다`(){
        assertThrows<IllegalArgumentException> {
            SeatRow("F")
        }
    }

    @Test
    fun `SeatRow는 빈값일 수 없다`(){
        assertThrows<IllegalArgumentException> {
            SeatRow("")
        }
    }
}
