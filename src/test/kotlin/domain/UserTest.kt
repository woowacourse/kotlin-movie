package domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

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
}
