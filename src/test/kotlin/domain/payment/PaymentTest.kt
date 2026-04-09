package domain.payment

import domain.account.Account
import domain.reservation.Cart
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PaymentTest {
    private val discountPolicy = DiscountPolicy()
    private val cart = Cart(emptyList())
    private val payment = Payment(
        cart = cart,
        discountPolicy = discountPolicy,
    )

    @Test
    fun `포인트 사용액이 결제 금액 이하이면 해당 금액만큼 차감된다`() {
        val account = Account()

        val result = payment.applyPoint(
            amount = 10_000,
            account = account,
            point = 2_000,
        )

        assertEquals(8_000, result)
    }
}