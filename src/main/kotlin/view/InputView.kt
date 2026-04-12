package view

class InputView {
    // 영화 예매 입력뷰
    fun movieReserveInput(): String {
        println("영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)")
        val answer = readln()

        return answer
    }

    // 영화 제목 입력 뷰
    fun movieTitleInput(): String {
        println("\n예매할 영화 제목을 입력하세요:")
        val answer = readln()

        return answer
    }

    // 날짜 입력 뷰
    fun dateInput(): String {
        println("\n날짜를 입력하세요 (YYYY-MM-DD):")
        val answer = readln()

        return answer
    }

    // 상영 번호 입력 뷰
    fun screeningNumberInput(size: Int): Int {
        println("상영 번호를 선택하세요:")
        val answer = readln().toIntOrNull() ?: throw IllegalArgumentException("잘못된 입력입니다")
        require(answer in 1..size) { "잘못된 번호입니다" }
        return answer
    }

    // 예약할 좌석 입력 뷰
    fun reserveSeatsInput(): String {
        println("\n예약할 좌석을 입력하세요 (A1, B2):")
        val answer = readln()
        return answer
    }

    // 다른 영화 추가 여부 뷰
    fun againMovieReserveInput(): String {
        println("\n다른 영화를 추가하시겠습니까? (Y/N)")
        val answer = readln()

        return answer
    }

    // 포인트 입력 뷰
    fun pointInput(): String {
        println("\n사용할 포인트를 입력하세요 (없으면 0):")
        return readln()
    }

    // 결제수단 입력 뷰
    fun paymentMethodInput(): String {
        println("\n결제 수단을 선택하세요:")
        println("1) 신용카드(5% 할인)")
        println("2) 현금(2% 할인)")
        return readln()
    }

    // 영수증 뷰
    fun paymentConfirmInput(): String {
        println("\n위 금액으로 결제하시겠습니까? (Y/N)")
        return readln()
    }
}
