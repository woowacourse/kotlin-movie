package model.cart

import model.discount.reserveDiscountPolicy.ReserveDiscountPolicy
import model.schedule.Screening
import model.seat.Price

// 장바구니
class Cart(
    val items: List<CartItem> = emptyList(),
) {
    // 선택한 영화 시간이 장바구니의 다른 영화 시간과 겹치는지 확인하는 함수
    fun isOverlapping(selectedScreening: Screening) {
        if (items.any {
                it.screening.movie.title != selectedScreening.movie.title &&
                    it.screening.isOverlapping(selectedScreening)
            }
        ) {
            throw IllegalArgumentException("선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요.")
        }
    }

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
