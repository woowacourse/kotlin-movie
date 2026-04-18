package api

import database.Database
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application {
    @PostConstruct
    fun init() {
        Database.init()
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
