package console.view

import model.discount.PaymentMethod
import java.time.LocalDate

class InputView {
    fun movieReserveInput(): Boolean {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val answer = readln()

        return yesOrNo(answer)
    }

    fun movieTitleInput(): String {
        println("\n예매할 영화 제목을 입력하세요:")
        val answer = readln()

        return answer
    }

    fun dateInput(): LocalDate {
        println("\n날짜를 입력하세요 (YYYY-MM-DD):")

        val dateRegex = """^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$""".toRegex()
        val answer = readln()

        if (!answer.matches(dateRegex)) {
            throw IllegalArgumentException("잘못된 날짜 입력입니다")
        }

        return LocalDate.parse(answer)
    }

    fun screeningNumberInput(size: Int): Int {
        println("상영 번호를 선택하세요:")
        val answer = readln().toIntOrNull() ?: throw IllegalArgumentException("잘못된 입력입니다")
        require(answer in 1..size) { "잘못된 번호입니다" }
        return answer
    }

    fun reserveSeatsInput(): String {
        println("\n예약할 좌석을 입력하세요 (A1, B2):")
        val answer = readln()
        return answer
    }

    fun againMovieReserveInput(): Boolean {
        println("\n다른 영화를 추가하시겠습니까? (Y/N)")
        val answer = readln()

        return yesOrNo(answer)
    }

    fun pointInput(): String {
        println("\n사용할 포인트를 입력하세요 (없으면 0):")
        return readln()
    }

    fun paymentMethodInput(): PaymentMethod {
        println("\n결제 수단을 선택하세요:")
        println("1) 신용카드(5% 할인)")
        println("2) 현금(2% 할인)")
        return choosePaymentMethod(readln())
    }

    fun paymentConfirmInput(): Boolean {
        println("\n위 금액으로 결제하시겠습니까? (Y/N)")
        val answer = readln()

        return yesOrNo(answer)
    }

    private fun yesOrNo(input: String): Boolean =
        when (input) {
            "Y" -> true
            "N" -> false
            else -> throw IllegalArgumentException("잘못된 입력입니다")
        }

    private fun choosePaymentMethod(input: String): PaymentMethod =
        when (input) {
            "1" -> PaymentMethod.CREDIT_CARD
            "2" -> PaymentMethod.CASH
            else -> throw IllegalArgumentException("잘못된 입력입니다")
        }
}
