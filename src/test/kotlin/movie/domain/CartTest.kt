package movie.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.LocalDateTime

class CartTest {
    @Test
    fun `예약이 정상적으로 추가된다`() {
        val movie = Movie("아이언맨", 200)
        val schedule = Schedule(
            movie = movie,
            startTime = LocalDateTime.of(2026, 4, 10, 10, 0),
            endTime = LocalDateTime.of(2026, 4, 10, 12, 40)
        )

        val cart = Cart()

        assertDoesNotThrow {
            cart.addReservation(schedule, emptyList())
        }
    }
}
