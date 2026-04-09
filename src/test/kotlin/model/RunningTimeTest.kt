package model

import model.movie.RunningTime
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class RunningTimeTest {
    @Test
    fun `상영 길이가 0 이하이면 예외가 발생한다`() {
        // given & when & then
        assertThrows(IllegalArgumentException::class.java) {
            RunningTime(0)
        }
    }
}
