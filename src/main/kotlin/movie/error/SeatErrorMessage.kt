package movie.error

object SeatErrorMessage {
    const val INVALID_ROW = "좌석 번호의 행은 A부터 Z사이의 알파뱃이어야 합니다."
    const val INVALID_COL = "좌석 번호의 열은 0보다 큰 숫자여야 합니다."
    const val COL_NOT_INTEGER = "좌석 열 번호는 정수여야 합니다."
    const val NOT_FOUND = "존재하지 않는 좌석입니다."
    const val INVALID_NUMBER = "유효하지 않은 좌석입니다."
    const val ALREADY_RESERVED = "이미 예약된 좌석입니다."
    const val EMPTY_INPUT = "좌석을 하나 이상 입력해야 합니다."
    const val INVALID_FORMAT = "좌석은 A1, B2 형식으로 입력해야 합니다."
}
