package console

import console.controller.Controller
import domain.screening.ScreeningSchedule
import repository.JdbcConnection
import repository.ScreeningRepository
import repository.SimpleDataSource
import java.io.File
import java.sql.Connection

fun main() {
    val connection = JdbcConnection.getConnection()
    initializeSchema(connection)
    
    val dataSource = SimpleDataSource()
    val screeningRepository = ScreeningRepository(dataSource)
    val screenings = screeningRepository.findAll()
    
    val schedule = ScreeningSchedule(screenings)
    Controller(schedule).run()
}

fun initializeSchema(connection: Connection) {
    val schemaSql = File("src/main/resources/schema.sql").readText()
    connection.createStatement().use { stmt ->
        stmt.execute(schemaSql)
    }
}
