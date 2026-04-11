package movie.view

import movie.domain.amount.Money
import movie.domain.amount.Point
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screen
import movie.domain.screening.Screening
import movie.domain.seat.ReservatedSeats
import movie.domain.seat.SeatColumn
import movie.domain.seat.SeatRow

class OutputView {
    fun printScreeningList(screenings: List<Screening>) {
        println(SCREENING_LIST_HEADER)
        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.slot.startTime}")
        }
    }

    fun printTimeOverlapMessage() {
        println(SCREENING_TIME_OVERLAP_MESSAGE)
    }

    fun printSeatLayout(
        screen: Screen,
        reservedSeats: ReservatedSeats,
    ) {
        println(SEAT_LAYOUT_HEADER)
        println("    1    2    3    4")
        val rows = listOf("A", "B", "C", "D", "E")
        for (row in rows) {
            print("$row ")
            for (col in 1..4) {
                val seat = screen.seats.findSeat(SeatRow(row), SeatColumn(col))
                if (reservedSeats.isAvailable(seat)) {
                    print("[ ${seat.grade}] ")
                } else {
                    print("[XX] ")
                }
            }
            println()
        }
    }

    fun printAddedToCart(reservation: Reservation) {
        println(ADDED_TO_CART_MESSAGE)
        printReservationItem(reservation)
    }

    fun printCart(reservations: Reservations) {
        println(CART_HEADER)
        println(reservations.display())
    }

    fun printFinalPrice(price: Money) {
        println(PRICE_CALCULATION_HEADER)
        println(FINAL_PRICE_FORMAT.format(formatMoney(price)))
    }

    fun printComplete(
        reservations: Reservations,
        price: Money,
        usedPoint: Point,
    ) {
        println(RESERVATION_COMPLETE_HEADER)
        println(RESERVATION_DETAIL_HEADER)
        println(reservations.display())
        println(
            PAYMENT_AMOUNT_FORMAT.format(
                formatMoney(price),
                formatMoney(
                    Money(
                        usedPoint.value,
                    ),
                ),
            ),
        )
        println(END_MESSAGE)
    }

    fun printErrorMessage(errorMessage: String) {
        println()
        println(errorMessage)
        println()
    }

    private fun printReservationItem(reservation: Reservation) {
        println(reservation.display())
    }

    private fun formatMoney(money: Money): String = String.format("%,d", money.value)

    companion object {
        private const val SCREENING_LIST_HEADER = "해당 날짜의 상영 목록"
        private const val SCREENING_TIME_OVERLAP_MESSAGE = "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."

        private const val SEAT_LAYOUT_HEADER = "좌석 배치도"

        private const val ADDED_TO_CART_MESSAGE = "장바구니에 추가됨"
        private const val CART_HEADER = "장바구니"

        private const val PRICE_CALCULATION_HEADER = "가격 계산"
        private const val FINAL_PRICE_FORMAT = "최종 결제 금액: %s원"

        private const val RESERVATION_COMPLETE_HEADER = "예매 완료"
        private const val RESERVATION_DETAIL_HEADER = "내역:"
        private const val PAYMENT_AMOUNT_FORMAT = "결제 금액: %s원  (포인트 %s원 사용)"
        private const val END_MESSAGE = "감사합니다."
    }
}
