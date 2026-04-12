@file:Suppress("NonAsciiCharacters")

package model.movie

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RunningTimeTest {
    @Test
    fun `상영 길이가 0 이하이면 예외가 발생한다`() {
        // given & when & then
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            RunningTime(0)
        }
    }
}
