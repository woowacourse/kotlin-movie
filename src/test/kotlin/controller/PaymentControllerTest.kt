package controller

import domain.Id
import domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ErrorMessage
import java.io.ByteArrayInputStream

class PaymentControllerTest {
    val controller = PaymentController()

    @Test
    fun `영수증 생성 시 좌석 별 할인 금액과 구매 내역이 저장된다`() {
        // given & when : 장바구니로 영수증을 생성하면
        val result = controller.createReceipt(TestFixtureData.cart)

        // then : 할인된 금액과 구매 내역이 영수증에 저장된다.
        assertThat(result.totalPrice()).isEqualTo(27_700)
        assertThat(result.purchaseHistory).containsExactlyElementsOf(TestFixtureData.cart.reservationInfos)
    }

    @Test
    fun `사용할 포인트가 보유 포인트보다 크면 예외가 발생한다`() {
        // given : 사용자의 포인트보다 더 큰 포인트가 입력된다.
        val input = "3000"
        val receipt = controller.createReceipt(TestFixtureData.cart)
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.getUserPoint(receipt, User(Id("user-1")))
            }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.POINT_DEDUCTION_EXCEEDS_BALANCE, exception.message)
    }

    @Test
    fun `결제 수단 입력이 유효하지 않으면 예외가 발생한다`() {
        // given : 3을 입력한다
        val input = "3"
        val receipt = controller.createReceipt(TestFixtureData.cart)
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 포인트를 처리하면
        val exception =
            assertThrows<IllegalArgumentException> {
                controller.getPaymentMethod(receipt)
            }

        // then : 예외가 발생한다.
        assertEquals(ErrorMessage.INVALID_PAYMENT_METHOD, exception.message)
    }

    @Test
    fun `결제 수단 할인이 영수증에 반영된다`() {
        // given : 결제 수단으로 신용카드가 제시된다.
        val input = "1"
        val receipt = controller.createReceipt(TestFixtureData.cart)
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제 수단을 적용하면
        val result = controller.getPaymentMethod(receipt)

        // then : 할인된 금액이 반환된다.
        assertThat(result.totalPrice()).isEqualTo(26_315)
    }

    @Test
    fun `모든 할인이 순서대로 적용된 최종 금액을 계산한다`() {
        // given : 각각의 입력값을 받는다.
        val input = "2000\n1\nY"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        // when : 결제를 적용하면
        val result = controller.run(TestFixtureData.cart, User(Id("user-1")))

        // then : 할인된 총 금액이 반환된다.
        assertThat(result.totalPrice()).isEqualTo(24_415)
        assertThat(result.usedPoint).isEqualTo(2_000)
    }

    @Test
    fun `포인트 입력 단계에서는 실제 포인트가 차감되지 않는다`() {
        val input = "500"
        val receipt = controller.createReceipt(TestFixtureData.cart)
        val user = User(Id("user-1"))
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        controller.getUserPoint(receipt, user)

        assertThat(user.point.value).isEqualTo(2_000)
    }

    @Test
    fun `결제 확정 시 포인트가 실제로 차감된다`() {
        val user = User(Id("user-2"))
        val receipt = controller.createReceipt(TestFixtureData.cart).applyPoint(user, "500")

        controller.confirmPayment(user, receipt)

        assertThat(user.point.value).isEqualTo(1_500)
    }

    @Test
    fun `결제 진행 중 잘못된 입력이 들어오면 올바른 입력이 들어올 때까지 다시 입력받는다`() {
        val user = User(Id("user-3"))
        val input = "abc\n3000\n500\n3\n1"
        System.setIn(ByteArrayInputStream(input.toByteArray()))

        val result = controller.run(TestFixtureData.cart, user)

        assertThat(result.totalPrice()).isEqualTo(25_840)
        assertThat(result.usedPoint).isEqualTo(500)
    }
}
