package domain.model.cart

import domain.model.screen.Screening

// 장바구니 항목 1건(상영 + 선택 좌석 문자열)을 표현한다.
data class CartItem(
    val screening: Screening,
    val seatCodes: List<String>,
) {
    // 장바구니 출력용 한 줄 문구를 만든다.
    fun displayLine(): String =
        "- [${screening.movie.title}] ${screening.screeningDate} ${screening.startTime}  좌석: ${seatCodes.joinToString(", ")}"
}
