package model.cart

import model.discount.reserveDiscountPolicy.ReserveDiscountPolicy
import model.seat.Price

// 장바구니
class Cart(
    val items: List<CartItem> = emptyList(),
) {
    // 장바구니에 영화를 담는 함수
    fun addItem(cartItem: CartItem): Cart = Cart(items + cartItem)

    // 각 항목별로 무비데이 + 시간 할인 적용 후 합산하는 함수
    fun calculateItemsPrice(reserveDiscountPolicy: ReserveDiscountPolicy): Price =
        Price(
            items.sumOf { item ->
                reserveDiscountPolicy
                    .calculatePrice(
                        price = item.screening.calculatePrice(item.seatNames),
                        reservedDateTime = item.screening.startDateTime,
                    ).value
            },
        )
}
