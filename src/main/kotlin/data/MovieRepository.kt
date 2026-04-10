package data

import model.movie.Movie
import model.movie.RunningTime

object MovieRepository {
    val F1_THE_MOVIE =
        Movie(
            title = "F1 더 무비",
            runningTime = RunningTime(160),
        )

    val TOY_STORY =
        Movie(
            title = "토이 스토리",
            runningTime = RunningTime(150),
        )

    val IRON_MAN =
        Movie(
            title = "아이언맨",
            runningTime = RunningTime(130),
        )

    val ROBOT_DREAM =
        Movie(
            title = "로봇 드림",
            runningTime = RunningTime(110),
        )
}
