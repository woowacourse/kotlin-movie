import db.DataSeeder
import db.DatabaseConfig
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackages = ["api", "config"])
class Application {
    @Bean
    fun init(): ApplicationRunner =
        ApplicationRunner {
            DatabaseConfig.initialize()
            DataSeeder.seed()
        }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
