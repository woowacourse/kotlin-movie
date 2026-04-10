package movie.domain

class Movie(
    val title: String,
    val runningTime: Int,
) {
    init {
        require(title.isNotBlank()) { "영화 제목은 공백일 수 없습니다." }
        require(runningTime > 0) { "영화 러닝 타임은 0분 이하일 수 없습니다." }
    }
}
