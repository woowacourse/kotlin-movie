package controller

import domain.model.cart.Cart
import domain.model.cart.CartItem
import domain.model.screen.Screening

class ReservationController(
    private val cart: Cart = Cart(),
) {
    // 예약 저장하기
    fun reserve(
        screening: Screening,
        seatCodes: List<String>,
    ): CartItem {
        val item =
            CartItem(
                screening = screening,
                seatCodes = normalizeSeatCodes(seatCodes),
            )
        cart.add(item)
        return item
    }

    // 예약 리스트별 제목, 날짜, 시작시간, 좌석 추출하기
    fun reservationSummaries(): List<String> =
        cart.items().map { item ->
            item.displayLine()
        }

    // 사용자가 고른 상영 시간이 기존 장바구니와 겹치는지 확인한다.
    fun hasOverlapping(screening: Screening): Boolean = cart.hasOverlapping(screening)

    // 좌석 문자열 입력값을 공백 제거 + 대문자로 통일한다.
    private fun normalizeSeatCodes(seatCodes: List<String>): List<String> =
        seatCodes.map { seatCode ->
            seatCode.trim().uppercase()
        }
}
