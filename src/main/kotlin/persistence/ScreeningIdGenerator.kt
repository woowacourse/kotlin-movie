package persistence

import domain.cinema.ScreeningSchedule

object ScreeningIdGenerator {
    fun generate(screening: ScreeningSchedule): String =
        "screening-${screening.movie.id.value}-${screening.screen.id.value}-${screening.startTime}"
}
