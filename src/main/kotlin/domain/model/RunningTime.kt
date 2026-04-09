package domain.model

@JvmInline
value class RunningTime(
    val duration: Int,
) {
    init {
        require(duration > 0) { "상영 길이는 음수일 수 없습니다." }
    }
}
