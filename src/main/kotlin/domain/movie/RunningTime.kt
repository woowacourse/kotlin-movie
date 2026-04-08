package domain.movie

@JvmInline
value class RunningTime(val minute: Long) {
    init {
        require(minute > 0) { "상영 길이는 0분 이하일 수 없습니다." }
    }
}
