package movie.domain.screening

data class ScreenId(
    val value: Int,
) {
    init {
        require(value > 0) { "상영관 ID는 0보다 커야 합니다." }
    }
}
