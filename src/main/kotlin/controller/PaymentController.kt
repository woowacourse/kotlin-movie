package controller

import domain.model.Payment.Discount
import domain.model.Payment.Pay
import domain.model.Payment.PaymentMethod
import domain.model.cart.CartItem

class PaymentController(
    private var point: Int = 0,
    private var paymentMethod: PaymentMethod = PaymentMethod.CARD,
) {

    // 사용할 포인트를 설정한다.
    fun usePoint(point: Int) {
        require(point >= 0) { "포인트는 0 이상이어야 합니다." }
        this.point = point
    }

    // 결제 수단을 선택한다.
    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        this.paymentMethod = paymentMethod
    }

    // 전체 적용
    // 1) 예매별 할인(무비데이, 시간) 적용
    // 2) 누적 금액에 포인트 차감 + 결제수단 할인 적용
    fun payAmountApply(items: List<CartItem>): Int {
        val discountAppliedAmount =
            items.sumOf { item ->
                Discount(
                    screening = item.screening,
                ).discountAmountApply(item.seatAmount())
            }

        return Pay(
            point = point,
            paymentMethod = paymentMethod,
        ).payAmountApply(discountAppliedAmount)
    }
}
