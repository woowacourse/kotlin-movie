package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class User(val id: Long, val point: Long) {
    init {
        require(id > 0) { "ID는 양수이어야 합니다." }
    }
}

class UserTest {
    @Test
    fun `ID는 양수일 때 정상 생성 되어야 한다`() {
        // given & when : User의 ID에 양수값을 넣는다.
        val result = User(1, 1000)

        // then : 정상 생성 된다.
        assertNotNull(result)
    }

    @Test
    fun `ID가 음수일 때 예외가 발생한다`() {
        // given & when : User의 ID에 음수값을 넣는다.
        val exception = assertThrows<IllegalArgumentException> {
            User(-1, 1000)
        }

        // then : 예외가 발생한다.
        assertEquals(exception.message, "ID는 양수이어야 합니다.")
    }
}
