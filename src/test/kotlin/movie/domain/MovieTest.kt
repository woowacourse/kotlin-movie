package movie.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class MovieTest {
    @Test
    fun `영화제목과 러닝타임이 정상이라면 영화 객체가 생성된다`() {
        assertDoesNotThrow {
            Movie(title = "아이언", runningTime = 715)
        }
    }

    @Test
    fun `영화 제목이 공백이라면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            Movie(title = "", runningTime = 120)
        }

        assertThrows<IllegalArgumentException> {
            Movie(title = "\t", runningTime = 120)
        }

        assertThrows<IllegalArgumentException> {
            Movie(title = "\n", runningTime = 120)
        }

        assertThrows<IllegalArgumentException> {
            Movie(title = "     ", runningTime = 120)
        }
    }

    @Test
    fun `러닝타임이 0분 이하라면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            Movie(title = "아이언", runningTime = 0)
        }

        assertThrows<IllegalArgumentException> {
            Movie(title = "아이언", runningTime = -10)
        }
    }
}
