@file:Suppress("NonAsciiCharacters")

package model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class MoneyTest {
    @Test
    fun `금액이 0 미만이면 예외가 발생한다`() {
        // given & when & then
        assertThrows(IllegalArgumentException::class.java) {
            Money(-1)
        }
    }

    @Test
    fun `금액이 0 미만일때 옳바른 예외 메세지가 출력된다`() {
        // given & when
        val errorMessage =
            assertThrows(IllegalArgumentException::class.java) {
                Money(-1)
            }.message

        // then
        assertThat(errorMessage).isEqualTo("금액은 음수가 될 수 없습니다.")
    }

    @Test
    fun `초기 금액이 500일때 생성된 Money의 value가 500이다`() {
        // given & when
        val actual = Money(500).value

        // then
        assertThat(actual).isEqualTo(500)
    }
}
