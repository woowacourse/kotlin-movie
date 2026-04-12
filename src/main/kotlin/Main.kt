@file:OptIn(ExperimentalUuidApi::class)

import model.CinemaData
import model.CinemaKiosk
import model.schedule.CinemaSchedule
import kotlin.uuid.ExperimentalUuidApi

fun main() {
    val screenSchedules = CinemaData.initScreenSchedule()
    val cinemaSchedule = CinemaSchedule(screenSchedules = screenSchedules)
    CinemaController(
        cinemaKiosk = CinemaKiosk(cinemaSchedule),
        movieCatalog = CinemaData.initMovieCatalog(),
    ).run()
}
