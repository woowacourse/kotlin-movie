package model.cart

import model.discount.DiscountBenefits
import model.discount.PaymentMethod
import model.schedule.Screening

// 장바구니
class Cart(
    val items: List<CartItem> = emptyList(),
) {
    // 장바구니에 영화를 담는 함수
    fun addItem(
        screening: Screening,
        seatNames: List<String>,
    ): Cart = Cart(items + CartItem(screening, seatNames))

    fun calculateTotalPrice(
        discountBenefits: DiscountBenefits,
        usePoint: Int,
        paymentMethod: PaymentMethod,
    ): Int {
        // 각 항목별로 무비데이 + 시간 할인 적용 후 합산
        val subtotal =
            items.sumOf { item ->
                var price = item.screening.calculatePrice(item.seatNames)
                price = discountBenefits.movieDay(price, item.screening.startDateTime.toLocalDate())
                price = discountBenefits.timeDiscount(price, item.screening.startDateTime.toLocalTime())
                price
            }

        // 합산 금액에 포인트 + 결제 방식 할인 적용
        var total = discountBenefits.pointDiscount(price = subtotal, usePoint = usePoint)

        total =
            when (paymentMethod) {
                PaymentMethod.CARD -> discountBenefits.cardDiscount(total)
                PaymentMethod.CASH -> discountBenefits.cashDiscount(total)
            }
        return total
    }
}
