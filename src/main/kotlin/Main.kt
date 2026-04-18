import controller.Controller
import database.DatabaseConfig
import database.DatabaseInitializer
import repository.ReservationRepository
import repository.ScreeningRepository

fun main() {
    DatabaseInitializer.init(DatabaseConfig.getConnection())
    val connection = DatabaseConfig.getConnection()
    val screeningRepository = ScreeningRepository(connection)
    val reservationRepository = ReservationRepository(connection)
    Controller(screeningRepository, reservationRepository).run()
}
