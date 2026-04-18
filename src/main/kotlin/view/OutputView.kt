package view

import domain.cinema.Screen
import domain.cinema.ScreeningSchedule
import domain.purchase.Receipt
import domain.reservation.ReservationInfo

object OutputView {
    fun printError(message: String) {
        println("[ERROR] $message")
        println()
    }

    fun printScreenings(screenings: List<ScreeningSchedule>) {
        println("해당 날짜의 상영 목록")

        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.startTime.time}")
        }
    }

    fun printSeats(screen: Screen) {
        val maxRow = Screen.MAX_ROW
        val maxColumn = Screen.MAX_COLUMN

        println("좌석 배치도")
        val header = " ".repeat(3) + (1..maxColumn).joinToString("    ")
        println(header)

        ('A' until 'A' + maxRow).forEach { row ->
            val line =
                "$row" +
                    (1..maxColumn).joinToString("") { col ->
                        val seat = screen.findSeat(row, col)
                        " [ ${seat?.grade?.name ?: " "}]"
                    }
            println(line)
        }
        println()
    }

    fun printCart(cart: List<ReservationInfo>) {
        println("장바구니")
        formatReservationInfos(cart).forEach {
            println(it)
        }
        println()
    }

    fun printTotalPrice(price: Int) {
        println("가격 계산")
        println("최종 결제 금액: ${printByDecimalFormat(price)}원")
        println()
    }

    fun printByDecimalFormat(price: Int): String = String.format("%,d", price)

    fun printTotal(receipt: Receipt) {
        println("예매완료")
        println("내역:")
        printCart(receipt.purchaseHistory)

        println("결제 금액: ${printByDecimalFormat(receipt.totalPrice())}원 (포인트 ${printByDecimalFormat(receipt.usedPoint)})")

        println()
        println("감사합니다.")
    }

    fun formatReservationInfos(reservationInfos: List<ReservationInfo>): List<String> =
        reservationInfos
            .groupBy { it.screening }
            .map { (screening, group) ->
                val seats = group.joinToString(", ") { "${it.seat.coordinate.row}${it.seat.coordinate.column}" }
                "- [${screening.movie.title}] ${screening.startTime.toString().replace("T", " ").substring(0, 16)} 좌석: $seats"
            }
}
