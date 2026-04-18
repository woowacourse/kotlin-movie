package movie.error

object MovieErrorMessage {
    const val BLANK_TITLE = "영화 제목은 비어있을 수 없습니다."
    const val INVALID_RUNNING_TIME = "영화 러닝 타임은 0분 이하일 수 없습니다."
    const val NOT_FOUND_ON_DATE = "해당 영화가 해당 날짜에 상영하지 않습니다."
    const val NO_MOVIES_IN_THEATER = "상영 중인 영화가 없습니다."
}
