package movie.api

import movie.database.DatabaseInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
    DatabaseInitializer.initTables(isLocal = false)

    runApplication<ApiApplication>(*args)
}
