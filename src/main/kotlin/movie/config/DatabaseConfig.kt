package movie.config

import jakarta.annotation.PostConstruct
import movie.data.db.DatabaseInitializer
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfig {
    @PostConstruct
    fun initialize() {
        DatabaseInitializer.initialize()
    }
}
