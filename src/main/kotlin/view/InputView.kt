package view

import model.CinemaTime
import model.payment.PayType
import model.seat.SeatColumn
import model.seat.SeatRow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object InputView {
    fun askStartReservation(): Boolean = inputYesOrNo(Message.START_RESERVATION)

    fun askReserveMore(): Boolean = inputYesOrNo(Message.INPUT_CONTINUE)

    private fun inputYesOrNo(text: String): Boolean {
        println(text)
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    fun inputMovieName(): String {
        println(Message.INPUT_MOVIE_NAME)
        return readln()
    }

    fun inputDate(): CinemaTime {
        println(Message.INPUT_DATE)
        val input = readln()
        val date =
            runCatching {
                LocalDate.parse(
                    input,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                )
            }.getOrElse {
                throw IllegalArgumentException("날짜 형식이 올바르지 않습니다. (YYYY-MM-DD)")
            }
        return CinemaTime(date.atStartOfDay())
    }

    fun selectMovieScreening(): Int {
        println(Message.SELECT_MOVIE_SCREENING)
        val input = readln()
        require(input.all { it.isDigit() }) { "숫자만 가능합니다" }
        return input.toInt()
    }

    fun selectSeats(): List<Pair<SeatRow, SeatColumn>> {
        println("예약할 좌석을 입력하세요 (A1, B2):")
        val rawSeats = readln().split(",").map { it.trim() }
        return rawSeats.map { rawSeat ->
            SeatRow(rawSeat[0].toString()) to SeatColumn(rawSeat[1].digitToInt())
        }
    }

    fun inputPoint(): Int {
        println(Message.INPUT_POINT)
        val point = readln()
        InputValidator.validateNumber(point)
        return point.toInt()
    }

    fun inputPaymentMethod(): Int {
        println(Message.SELECT_PAYMENT_METHOD)
        PayType.entries.forEach { payType ->
            println(displayPayType(payType))
        }
        val input = readln()
        InputValidator.validateType(input)
        return input.toInt()
    }

    fun inputConfirmPayment(): Boolean {
        println(Message.INPUT_CONFIRM_PAYMENT)
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    private fun displayPayType(payType: PayType) =
        when (payType) {
            PayType.CREDIT_CARD -> "1) 신용카드(5% 할인)"
            PayType.CASH -> "2) 현금(2% 할인)"
        }
}
