package view

import model.Point
import model.payment.Card
import model.payment.Cash
import model.payment.PaymentMethod
import model.seat.SeatNumber
import java.time.LocalDate

object InputView {
    fun readStartReservation(): Boolean {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        return trimInput()?.uppercase() == "Y"
    }

    fun readMovieTitle(): String {
        println("\n예매할 영화 제목을 입력하세요:")
        return trimInput() ?: ""
    }

    fun readDate(): LocalDate {
        println("\n날짜를 입력하세요 (YYYY-MM-DD):")
        return LocalDate.parse(trimInput() ?: "")
    }

    fun readScreeningNumber(): Int {
        println("상영 번호를 선택하세요:")
        return trimInput()?.toIntOrNull() ?: 0
    }

    fun readSeatsNumber(): List<SeatNumber> {
        println("\n예약할 좌석을 입력하세요 (예: A1, B2):")
        val input = trimInput() ?: return emptyList()

        return input.split(",").map { it.trim() }.map {
            SeatNumber(row = it[0], column = it.substring(1).toInt())
        }
    }

    fun readAddMoreMovie(): Boolean {
        println("\n다른 영화를 추가하시겠습니까? (Y/N)")
        return trimInput()?.uppercase() == "Y"
    }

    fun readPoint(): Point {
        println("\n사용할 포인트를 입력하세요(없으면 0):")
        val value = trimInput()?.toIntOrNull() ?: 0
        return Point(value)
    }

    fun readPaymentMethod(): PaymentMethod {
        println("\n결제 수단을 선택하세요:")
        println("1) 신용카드(5% 할인)")
        println("2) 현금(2% 할인)")
        return when (trimInput()?.toIntOrNull()) {
            1 -> Card
            2 -> Cash
            else -> throw IllegalArgumentException("잘못된 입력입니다.")
        }
    }

    fun readPaymentConfirmation(): Boolean {
        println("\n위 금액으로 결제하시겠습니까? (Y/N)")
        return trimInput()?.uppercase() == "Y"
    }

    private fun trimInput(): String? = readlnOrNull()?.trim()
}
