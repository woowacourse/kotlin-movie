package movie.error

object ScheduleErrorMessage {
    const val DUPLICATE_TIME = "상영시간은 중복될 수 없습니다."
    const val INVALID_TIME_RANGE = "영화 시작 시각은 종료 시각보다 빨라야 합니다."
    const val INVALID_MOVIE_OR_TIME = "영화 제목 또는 시작 시간이 올바르지 않습니다."
    const val SCREENING_NOT_FOUND = "존재하지 않는 상영 정보입니다."
    const val DUPLICATE_SELECTION_TIME = "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요."
}
