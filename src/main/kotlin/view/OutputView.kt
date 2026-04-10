package view

import model.Money
import model.payment.PayResult
import model.reservation.Reservation
import model.reservation.Reservations
import model.screening.Screenings
import model.seat.SeatNumber
import model.seat.Seats
import java.time.format.DateTimeFormatter

object OutputView {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun printScreenings(screenings: Screenings) {
        println("\n해당 날짜의 상영 목록")
        screenings.forEachIndexed { index, screening ->
            println("[${index + 1}] ${screening.startShowTime.format(timeFormatter)}")
        }
    }

    fun printSeatMap(
        defaultSeats: Seats,
        availableSeats: Seats,
    ) {
        println("\n좌석 배치도")

        val rows = 'A'..'E'
        val columns = 1..4

        println("    " + columns.joinToString(separator = "    ") { "$it" })
        rows.forEach { row ->
            val rowString = "$row " + columns.joinToString(" ") { col ->
                val seatNumber = SeatNumber(row, col)
                printSeatMarker(seatNumber, defaultSeats, availableSeats)
            }
            println(rowString)
        }
    }

    fun printCartItem(reservation: Reservation) {
        println("\n장바구니에 추가됨")
        println(formatReservation(reservation))
    }

    fun printCart(reservations: Reservations) {
        println("\n장바구니")
        reservations.forEach { println(formatReservation(it)) }
    }

    fun printFinalPrice(price: Money) {
        println("\n가격 계산")
        println("최종 결제 금액: ${formatMoney(price)}원")
    }

    fun printMovieNotSearch() {
        println("\n존재하지 않는 영화입니다.")
    }

    fun printError(message: String) {
        println("\n$message")
    }

    fun printReceipt(
        reservations: Reservations,
        payResult: PayResult,
    ) {
        println("\n예매 완료")
        println("내역:")
        reservations.forEach { println(formatReservation(it)) }
        val pointString = "(포인트 ${formatMoney(payResult.usedPoint.convertToMoney())}원 사용)"
        println("결제 금액: ${formatMoney(payResult.finalPrice)}원 $pointString")
        println("\n감사합니다.")
    }

    private fun printSeatMarker(
        seatNumber: SeatNumber,
        defaultSeats: Seats,
        availableSeats: Seats,
    ): String {
        if (seatNumber in availableSeats.seatNumbers) {
            val seatsGrade = defaultSeats.findSeat(seatNumber).seatGrade.name
            return "[ $seatsGrade]"
        }
        return "[ X]"
    }

    private fun formatReservation(reservation: Reservation): String {
        val movieTitle = reservation.movieTitle()
        val startDateTime = reservation.startDateTime().format(dateTimeFormatter)
        val seatNumbers = reservation.seats.seatNumbers.joinToString(", ")
        return "- [$movieTitle] $startDateTime  좌석: $seatNumbers"
    }

    private fun formatMoney(money: Money): String = "%,d".format(money.value)
}
