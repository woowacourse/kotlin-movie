package movie

import movie.database.DatabaseInitializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieApplication

fun main(args: Array<String>) {
    DatabaseInitializer.initSchema()
    runApplication<MovieApplication>(*args)
}
