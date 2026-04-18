@file:OptIn(ExperimentalUuidApi::class)

import database.Database
import database.repository.MovieScreeningRepository
import database.repository.ReservationRepository
import model.CinemaKiosk
import kotlin.uuid.ExperimentalUuidApi

fun main() {
    Database.init()
    CinemaController(
        cinemaKiosk = CinemaKiosk(),
        screeningRepository = MovieScreeningRepository(),
        reservationRepository = ReservationRepository(),
    ).run()
}
