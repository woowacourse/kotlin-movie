class MovieSchedule(
    vararg scheduledScreen: ScheduledScreen,
) {
    init {
        require(scheduledScreen.distinctBy { it.movie }.size <= 1) {
            "일정에 포함된 영화들은 모두 동일한 영화여야 합니다."
        }
    }
}
