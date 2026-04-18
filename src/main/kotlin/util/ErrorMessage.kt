package util

object ErrorMessage {
    const val INVALID_INPUT = "입력된 값이 유효하지 않습니다."
    const val INVALID_YES_OR_NO_INPUT = "입력값은 Y 혹은 N이어야 합니다."
    const val MOVIE_NOT_FOUND = "존재하지 않는 영화입니다."
    const val INVALID_DATE_FORMAT = "올바른 날짜 형식이 아닙니다. (YYYY-MM-DD)"
    const val SCREENING_NOT_FOUND_FOR_DATE = "해당 날짜에 선택한 영화의 상영이 없습니다."
    const val INVALID_SCREENING_NUMBER = "선택하신 상영 번호는 없는 상영 번호입니다."
    const val OVERLAPPING_SCREENING = "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."
    const val INVALID_SEAT_INPUT = "입력된 값이 유효하지 않습니다."
    const val INVALID_POINT_INPUT = "포인트는 0 이상의 숫자여야 합니다."
    const val SEAT_NOT_FOUND = "해당 상영관에는 해당 좌석이 존재하지 않습니다."
    const val SEAT_ALREADY_RESERVED = "해당 좌석은 이미 예약되었습니다."
    const val ID_MUST_NOT_BE_BLANK = "ID는 빈 값일 수 없습니다."
    const val POINT_DEDUCTION_EXCEEDS_BALANCE = "차감액은 전체 포인트보다 작아야 합니다."
    const val ROW_MUST_BE_UPPERCASE = "열은 한 글자 대문자 알파벳이여야 합니다."
    const val COLUMN_MUST_BE_POSITIVE = "행은 양수이여야 합니다."
    const val INVALID_PAYMENT_METHOD = "유효하지 않은 결제 수단입니다."
    const val SCREENING_NOT_FOUND = "존재하지 않는 상영입니다."
}
