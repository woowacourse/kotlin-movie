import domain.Id
import domain.purchase.PaymentMethod
import domain.purchase.Receipt
import domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ReceiptTest {
    @Test
    fun `영수증은 구매 내역을 저장하고 좌석별 할인 금액을 합산한다`() {
        val receipt = Receipt(TestFixtureData.reservationInfos)

        assertThat(receipt.purchaseHistory).containsExactlyElementsOf(TestFixtureData.reservationInfos)
        assertThat(receipt.totalPrice()).isEqualTo(27_700)
    }

    @Test
    fun `포인트 사용 금액이 영수증에 반영된다`() {
        val user = User(Id("user-1"))
        val receipt = Receipt(TestFixtureData.reservationInfos)

        val result = receipt.applyPoint(user, "2000")

        assertThat(result.usedPoint).isEqualTo(2_000)
        assertThat(result.totalPrice()).isEqualTo(25_700)
        assertThat(user.point.value).isEqualTo(2_000)
    }

    @Test
    fun `결제 수단 할인이 포인트 차감 후 금액에 적용된다`() {
        val receipt =
            Receipt(TestFixtureData.reservationInfos)
                .applyPaymentMethod(PaymentMethod.CARD)

        assertThat(receipt.totalPrice()).isEqualTo(26_315)
    }

    @Test
    fun `결제 확정 시 사용 포인트가 실제 차감된다`() {
        val user = User(Id("user-2"))
        val receipt = Receipt(TestFixtureData.reservationInfos).applyPoint(user, "500")

        receipt.confirm(user)

        assertThat(user.point.value).isEqualTo(1_500)
    }
}
