package controller

import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReservationController {
    fun run() {
    }

    fun create() {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val input = readln()

        require(input == "Y" || input == "N") { "입력값은 Y 혹은 N이어야 합니다." }
    }
}

class ReservationControllerTest {

    val controller = ReservationController()

    @Test
    fun `생성 여부가 Y,N이 아닐 경우 예외가 발생한다`() {
        // given : 생성 여부가 X로 입력된다.
        val input = "X"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 입력 여부를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.create()
        }

        // then : 예외가 발생한다.
        assertEquals("입력값은 Y 혹은 N이어야 합니다.", exception.message)
    }
}
