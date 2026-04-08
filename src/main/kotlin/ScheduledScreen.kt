data class ScheduledScreen(
    val movie: Movie,
    val cinemaScreen: CinemaScreen,
    val screenTime: DateTimeRange,
) {
    init {
        require(cinemaScreen.isContainServicePeriod(screenTime.start)) { "상영관의 운영 시작 시간보다 일찍 영화를 배정할 수 없습니다." }
        require(cinemaScreen.isContainServicePeriod(screenTime.end)) { "상영관의 운영 종료 시간보다 늦게 영화를 배정할 수 없습니다." }
        require(movie.runningTime.isSameDuration(screenTime)) { "영화의 러닝타임과 상영관의 상영 시간이 일치하지 않습니다." }
    }
}
