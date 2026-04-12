package model.discount

import model.discount.payDiscountPolicy.PayDiscountBenefits
import model.discount.payDiscountPolicy.PaymentPayDiscountPolicy
import model.discount.payDiscountPolicy.PointPayDiscountPolicy
import model.seat.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PayDiscountBenefitsTest {
    @Test
    fun `총 금액 계산 시 포인트 할인은 합산 금액에 한 번만 적용된다`() {
        val moviePrice = Price(42_000)
        val usePoint = 2000

        val totalPrice =
            PayDiscountBenefits(
                listOf(PointPayDiscountPolicy(usePoint)),
            ).calculatePrice(moviePrice)

        assertThat(totalPrice.value).isEqualTo(40_000)
    }

    @Test
    fun `총 금액 계산 시 결제 수단 할인은 포인트 할인 후 금액에 적용된다`() {
        val moviePrice = Price(42_000)
        val usePoint = 2000
        val paymentMethod = PaymentMethod.CARD

        val totalPrice =
            PayDiscountBenefits(
                payDiscountPolicies =
                    listOf(
                        PointPayDiscountPolicy(usePoint),
                        PaymentPayDiscountPolicy(paymentMethod),
                    ),
            ).calculatePrice(moviePrice)

        assertThat(totalPrice.value).isEqualTo(38_000)
    }
}
