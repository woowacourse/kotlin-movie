package domain.movie

import domain.screening.ScreeningPeriod

data class Movie(
    val id: Long? = null,
    val title: Title,
    val runningTime: RunningTime,
    val screeningPeriod: ScreeningPeriod,
)
