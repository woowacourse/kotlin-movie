package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class User(val id: Long) {
    var point: Long = 1000

    init {
        require(id > 0) { "ID는 양수이어야 합니다." }
    }

    fun discountPoint(discount: Long) {
        require(point - discount > 0) { "차감액은 전체 포인트보다 작아야 합니다." }
        point -= discount
    }
}

class UserTest {
    @Test
    fun `ID는 양수일 때 정상 생성 되어야 한다`() {
        // given & when : User의 ID에 양수값을 넣는다.
        val result = User(1)

        // then : 정상 생성 된다.
        assertNotNull(result)
    }

    @Test
    fun `ID가 음수일 때 예외가 발생한다`() {
        // given & when : User의 ID에 음수값을 넣는다.
        val exception = assertThrows<IllegalArgumentException> {
            User(-1)
        }

        // then : 예외가 발생한다.
        assertEquals(exception.message, "ID는 양수이어야 합니다.")
    }

    @Test
    fun `포인트 사용 시 포인트가 차감된다`() {
        // given : User 객체가 주어진다
        val result = User(1)

        // when : discountPoint로 500을 입력했을 때
        result.discountPoint(500)

        // then : 전체 포인트 값이 500이 된다.
        assertEquals(result.point, 500)
    }

    @Test
    fun `보유 포인트보다 많은 금액을 차감하면 예외가 발생한다`() {
        // given : User 객체가 주어진다
        val result = User(1)

        // when : discountPoint로 1500을 입력했을 때
        val exception = assertThrows<IllegalArgumentException> {
            result.discountPoint(1500)
        }

        // then : 예외가 발생 한다.
        assertEquals(exception.message, "차감액은 전체 포인트보다 작아야 합니다.")
    }
}
