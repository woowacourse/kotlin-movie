package controller

import domain.Calculator
import domain.Cart
import domain.PaymentMethod
import domain.User
import java.io.ByteArrayInputStream
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentController(val cart: Cart, val user: User) {
    fun run() {
    }

    fun getUserPoint(totalPrice: Int): Int {
        println("사용할 포인트를 입력하세요 (없으면 0):")
        val input = readln()

        user.discountPoint(input.toLong())

        val finalPrice = totalPrice - input.toInt()
        return finalPrice
    }

    fun discountPerSeat(): Int {
        val discountPrice = cart.reservationInfos.map {
            val initPrice = it.seat.grade.price
            Calculator.calculateByMovie(
                price = initPrice,
                date = it.showing.startTime,
            )
        }
        return discountPrice.sum()
    }

    fun getPaymentMethod(price: Int): Int {
        println(
            """
            결제 수단을 선택하세요:
            1) 신용카드(5% 할인)
            2) 현금(2% 할인)
            """.trimIndent(),
        )
        val input = readln()

        require(input.toInt() in 1..2) { "유효하지 않은 결제 수단입니다." }

        val method = PaymentMethod.entries.first { it.ordinal == input.toInt() }

        return Calculator.applyPaymentDiscount(price, method)
    }
}

class PaymentControllerTest {

    val controller = PaymentController(
        cart = TestFixtureData.cart,
        user = TestFixtureData.users.first(),
    )

    @Test
    fun `좌석 별로 무비데이 할인(10%)과 시간 할인(2,000원)이 적용된다`() {
        // given & when : 무비데이 할인과 시간 할인을 적용한다.
        val result = controller.discountPerSeat()

        // then : 할인된 금액이 반환된다.
        assertEquals(27700, result)
    }

    @Test
    fun `사용할 포인트가 보유 포인트보다 크면 예외가 발생한다`() {
        // given : 사용자의 포인트보다 더 큰 포인트가 입력된다.
        val input = "3000"
        val price = 27_700
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.getUserPoint(price)
        }

        // then : 예외가 발생한다.
        assertEquals("차감액은 전체 포인트보다 작아야 합니다.", exception.message)
    }

    @Test
    fun `결제 수단 입력이 유효하지 않으면 예외가 발생한다`() {
        // given : 3을 입력한다
        val input = "3"
        val price = 27_700
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception = assertThrows<IllegalArgumentException> {
            controller.getPaymentMethod(price)
        }

        // then : 예외가 발생한다.
        assertEquals("유효하지 않은 결제 수단입니다.", exception.message)
    }

    @Test
    fun `결제 수단 할인(신용카드 5%, 현금 2%)이 적용된다`() {
        // given : 결제 수단으로 신용카드가 제시된다.
        val input = "1"
        val price = 27_700
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제 수단을 적용하면
        val result = controller.getPaymentMethod(price)

        // then : 할인된 금액이 반환된다.
        assertEquals(27146, result)
    }
}
