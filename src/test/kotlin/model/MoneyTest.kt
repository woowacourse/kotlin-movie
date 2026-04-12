@file:Suppress("NonAsciiCharacters")

package model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class MoneyTest {
    @ParameterizedTest
    @ValueSource(ints = [-1, -10, -100, -9999])
    fun `금액이 0 미만이면 올바른 예외 메시지와 함께 예외가 발생한다`(invalidValue: Int) {
        // given & when & then
        assertThatThrownBy { Money(invalidValue) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("금액은 음수가 될 수 없습니다.")
    }

    @Test
    fun `초기 금액이 500일때 생성된 Money의 value가 500이다`() {
        // given & when
        val actual = Money(500).value

        // then
        assertThat(actual).isEqualTo(500)
    }
}
