package view

import model.CinemaTime
import model.movie.MovieName
import model.payment.PayType
import model.schedule.MovieSchedule
import model.schedule.MovieScreening
import model.seat.SeatColumn
import model.seat.SeatRow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object InputView {
    fun startMovieReservation(text: String): Boolean {
        println(text)
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    fun inputMovieName(): MovieName {
        println("예매할 영화 제목을 입력하세요:")
        return MovieName(readln())
    }

    fun inputDate(): CinemaTime {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
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

    fun selectMovieScreening(movieSchedule: MovieSchedule): MovieScreening {
        val input = readln()
        require(input.all { it.isDigit() }) { "숫자만 가능합니다" }
        val index = input.toInt() - 1
        require(index in (0..<movieSchedule.size)) { "올바르지 않은 번호입니다." }
        return movieSchedule.sortedBy { it.screenTime.start }[index]
    }

    fun selectSeats(): List<Pair<SeatRow, SeatColumn>> {
        println("예약할 좌석을 입력하세요 (A1, B2):")
        val rawSeats = readln().split(",").map { it.trim() }
        return rawSeats.map { rawSeat ->
            SeatRow(rawSeat[0].toString()) to SeatColumn(rawSeat[1].digitToInt())
        }
    }

    fun inputContinue(): Boolean {
        println("다른 영화를 추가하시겠습니까? (Y/N)")
        val input = readln()
        InputValidator.validateYesOrNo(input)
        return input == "Y"
    }

    fun inputPoint(): Int {
        println("사용할 포인트를 입력하세요 (없으면 0):")
        val point = readln()
        InputValidator.validateNumber(point)
        return point.toInt()
    }

    fun inputPaymentMethod(): Int {
        println("결제 수단을 선택하세요:")
        PayType.entries.forEach { payType ->
            println(displayPayType(payType))
        }
        val input = readln()
        InputValidator.validateType(input)
        return input.toInt()
    }

    fun inputConfirmPayment(): Boolean {
        println("위 금액으로 결제하시겠습니까? (Y/N)")
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
