package model

data class MovieScreening(
    val movie: Movie,
    val screenTime: DateTimeRange,
) {
    init {
        require(movie.runningTime.isSameDuration(screenTime)) { "영화의 러닝타임과 상영관의 상영 시간이 일치하지 않습니다." }
    }
}
