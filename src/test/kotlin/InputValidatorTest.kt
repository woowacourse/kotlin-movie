import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class InputValidatorTest {
    @Test
    fun `Y, N 이외에 값이 입력되면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            InputValidator.validateYesOrNo("Q")
        }
    }

    @Test
    fun `Y가 입력되면 예외가 발생하지 않는다`() {
        assertDoesNotThrow {
            InputValidator.validateYesOrNo("Y")
        }
    }

    @Test
    fun `N이 입력되면 예외가 발생하지 않는다`() {
        assertDoesNotThrow {
            InputValidator.validateYesOrNo("N")
        }
    }
}
