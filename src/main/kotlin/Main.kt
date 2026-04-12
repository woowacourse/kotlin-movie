@file:OptIn(ExperimentalUuidApi::class)

import model.CinemaData
import model.CinemaKiosk
import model.CinemaTime
import model.CinemaTimeRange
import model.movie.Movie
import model.movie.MovieCatalog
import model.movie.MovieId
import model.movie.MovieName
import model.movie.RunningTime
import model.schedule.CinemaSchedule
import model.schedule.MovieScreening
import model.schedule.ScreenSchedule
import model.seat.Seat
import model.seat.SeatColumn
import model.seat.SeatGrade
import model.seat.SeatGroup
import model.seat.SeatRow
import model.seat.SeatState
import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun main() {
    val screenSchedules = CinemaData.initScreenSchedule()
    val cinemaSchedule = CinemaSchedule(screenSchedules = screenSchedules)
    CinemaController(
        cinemaKiosk = CinemaKiosk(cinemaSchedule),
        movieCatalog = CinemaData.initMovieCatalog()
    ).run()
}
