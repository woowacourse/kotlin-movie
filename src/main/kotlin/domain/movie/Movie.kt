package domain.movie

import domain.screening.ScreeningPeriod

data class Movie(
    val title: Title,
    val runningTime: RunningTime,
    val screeningPeriod: ScreeningPeriod,
)
