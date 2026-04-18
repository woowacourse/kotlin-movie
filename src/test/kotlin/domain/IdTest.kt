package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage

class IdTest {
    @Test
    fun `ID는 빈 값이 아닐 때 정상 생성 되어야 한다`() {
        val result = assertDoesNotThrow { Id("movie-1") }

        assertEquals("movie-1", result.value)
    }

    @Test
    fun `ID가 빈 값이면 예외가 발생한다`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                Id("   ")
            }

        assertEquals(ErrorMessage.ID_MUST_NOT_BE_BLANK, exception.message)
    }
}
