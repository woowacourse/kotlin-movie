package api.config

import api.service.MovieQueryService
import db.DataInitializer
import db.JdbcMovieRepository
import db.JdbcReservationRepository
import db.JdbcScreeningRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repository.MovieRepository
import repository.ReservationRepository
import repository.ScreeningRepository
import java.sql.DriverManager

@Configuration
class AppConfig(
    @Value("\${db.url:jdbc:h2:~/kotlin-movie}") private val dbUrl: String,
) {
    private val connection = DriverManager.getConnection(dbUrl, "sa", "")

    @Bean
    fun dataInitializer(): ApplicationRunner = ApplicationRunner { DataInitializer(connection).initialize() }

    @Bean
    fun movieRepository(): MovieRepository = JdbcMovieRepository(connection)

    @Bean
    fun screeningRepository(): ScreeningRepository = JdbcScreeningRepository(connection)

    @Bean
    fun reservationRepository(
        movieRepository: MovieRepository,
        screeningRepository: ScreeningRepository,
    ): ReservationRepository = JdbcReservationRepository(connection, movieRepository, screeningRepository)

    @Bean
    fun movieQueryService(): MovieQueryService = MovieQueryService(connection)
}
