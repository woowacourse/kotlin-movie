package movie

import movie.controller.MovieController
import movie.database.DatabaseFactory
import movie.database.DatabaseInitializer
import movie.repository.ReservationRepository
import movie.repository.ScheduleRepository
import movie.service.ReservationService
import movie.service.ScheduleService

fun main() {
    DatabaseInitializer.initSchema()

    DatabaseFactory.getConnection().use { connection ->
        val scheduleRepository = ScheduleRepository(connection)
        val scheduleService = ScheduleService(scheduleRepository)
        val reservationRepository = ReservationRepository(connection)
        val reservationService = ReservationService(reservationRepository)

        val controller = MovieController(
            scheduleService = scheduleService,
            reservationService = reservationService
        )
        controller.run()
    }
}
