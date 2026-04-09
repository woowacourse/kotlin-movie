package view

object InputView {
    fun readLine(): String {
        val input = readln()
        println()
        return input
    }

    fun startTicketing(): String {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val input = readLine()
        return input
    }

    fun continueTicketing(): String {
        println("다른 영화를 추가하시겠습니까? (Y/N)")
        val input = readLine()
        return input
    }

    fun readMovieTitle(): String {
        println("예매할 영화 제목을 입력하세요:")
        val input = readLine()
        return input
    }

    fun readDate(): String {
        println("날짜를 입력하세요 (YYYY-MM-DD):")
        val input = readLine()
        return input
    }

    fun readShowingNumber(): String {
        println("상영 번호를 선택하세요:")
        val input = readLine()
        return input
    }

    fun readSeat(): String {
        println("예약할 좌석을 입력하세요 (A1, B2):")
        val input = readLine()
        return input
    }

    fun readPurchaseConfirm(): String {
        println("위 금액으로 결제하시겠습니까? (Y/N)")
        val input = readLine()
        return input
    }

    fun readPoint(): String {
        println("사용할 포인트를 입력하세요 (없으면 0):")
        val input = readLine()
        return input
    }

    fun readPaymentMethod(): String {
        println(
            """
            결제 수단을 선택하세요:
            1) 신용카드(5% 할인)
            2) 현금(2% 할인)
            """.trimIndent(),
        )
        val input = readLine()
        return input
    }
}
