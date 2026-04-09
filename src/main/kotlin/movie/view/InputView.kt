package movie.view

import java.time.LocalDate

class InputView {

    fun startMessage():Boolean{
        println(START_MESSAGE)
        return readYesNo()
    }

    fun inputMovieTitle():String{
        println(INPUT_MOVIE_TITLE_MESSAGE)
        return readInput()
    }

    fun inputDate(): LocalDate {
        println(INPUT_DATE_MESSAGE)
        return LocalDate.parse(readInput())
    }

    fun inputScreeningNumber():Int{
        println(INPUT_SCREENING_NUMBER_MESSAGE)
        return readInt()
    }

    fun inputSeat():String{
        println(INPUT_SEAT_MESSAGE)
        return readInput()
    }

    fun addMoreMovie():Boolean{
        println(ADD_MORE_MOVIE_MESSAGE)
        return readYesNo()
    }

    fun inputPoint():Int{
        println(INPUT_POINT_MESSAGE)
        return readInt()
    }

    fun inputPayment(): Int{
        println(SELECT_PAYMENT_METHOD_MESSAGE)
        println(CREDIT_CARD_OPTION)
        println(CASH_OPTION)
        return readInt()
    }

    fun confirmPayment():Boolean{
        println(CONFIRM_PAYMENT_MESSAGE)
        return readYesNo()
    }


    private fun readInput():String{
        return readlnOrNull()?.trim()
            ?: throw IllegalArgumentException("입력이 없습니다.")
    }
    private fun readYesNo():Boolean{
        require(readInput().uppercase() == "Y" || readInput().uppercase() == "N") {
            "Y 또는 N을 입력해 주세요."
        }
        return readInput().uppercase() == "Y"
    }
    private fun readInt():Int{
        return readInput().toIntOrNull()
            ?: throw IllegalArgumentException("숫자를 입력해주세요.")
    }

    companion object {

        //예매 시작 및 영화, 날짜 선택
        private const val START_MESSAGE = "영화 예매를 시작합니다. 새 예매를 생성하시겠습니까? (Y/N)"
        private const val INPUT_MOVIE_TITLE_MESSAGE = "예매할 영화 제목을 입력하세요:"
        private const val INPUT_DATE_MESSAGE = "날짜를 입력하세요 (YYYY-MM-DD):"

        //상영 선택
        private const val INPUT_SCREENING_NUMBER_MESSAGE = "상영 번호를 선택하세요:"

        //좌석 배치도
        private const val INPUT_SEAT_MESSAGE = "예약할 좌석을 입력하세요 (A1, B2):"

        // 장바구니
        private const val ADD_MORE_MOVIE_MESSAGE = "다른 영화를 추가하시겠습니까? (Y/N)"

        // 포인트
        private const val INPUT_POINT_MESSAGE = "사용할 포인트를 입력하세요 (없으면 0):"

        // 결제 수단
        private const val SELECT_PAYMENT_METHOD_MESSAGE = "결제 수단을 선택하세요:"
        private const val CREDIT_CARD_OPTION = "1) 신용카드(5% 할인)"
        private const val CASH_OPTION = "2) 현금(2% 할인)"

        private const val CONFIRM_PAYMENT_MESSAGE = "위 금액으로 결제하시겠습니까? (Y/N)"

    }

}
