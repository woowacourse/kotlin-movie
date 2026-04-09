package movie.domain.screening

import movie.domain.seat.Seats
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ScreenTest {
    @Test
    fun `상영관은 상영관ID와 좌석 목록을 갖는다`() {
        val screen = Screen(1, seats = Seats.createDefault())

        assert(screen.id == 1)
        assert(screen.seats.size() == 20)
    }

    @Test
    fun `상영관 번호는 0보다 커야 한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Screen(0, Seats.createDefault())
            }
        assert(exception.message == "상영관 ID는 0보다 커야 합니다.")
    }
}
