package domain.model.cart

import domain.model.screen.Screening
import domain.model.seat.Seat
import domain.model.seat.SeatClass

// 장바구니 항목 1건(상영 + 선택 좌석 목록)을 표현한다.
data class CartItem(
    val screening: Screening,
    val seats: List<Seat>,
) {
    // 장바구니 출력용 한 줄 문구를 만든다.
    fun displayLine(): String =
        "- [${screening.movie.title}] ${screening.screeningDate} ${screening.startTime}  좌석: ${seatCodes().joinToString(", ")}"

    // 장바구니 항목 1건의 좌석 총 금액을 계산한다.
    fun seatAmount(): Int =
        seats.sumOf { seat ->
            SeatClass.toPrice(seat.seatClass)
        }

    private fun seatCodes(): List<String> =
        seats.map { seat ->
            "${seat.row.name}${seat.column}"
        }
}
