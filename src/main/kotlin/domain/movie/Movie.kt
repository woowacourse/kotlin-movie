package domain.movie

import domain.movie.itmes.RunningTime
import domain.movie.itmes.ScreeningPeriod
import domain.movie.itmes.Title
import java.time.LocalDate

class Movie(
    private val title: Title,
    private val runningTime: RunningTime,
    private val screeningPeriod: ScreeningPeriod,
) {
    fun isValidTitle(title: String): Boolean = this.title.isSame(title)

    fun isScreening(date: LocalDate): Boolean = this.screeningPeriod.isContain(date)

    fun getTitle() = title.getTitle()
}
