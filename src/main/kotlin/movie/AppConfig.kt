package movie

import movie.domain.discount.DiscountPolicies
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.TimeDiscount
import movie.domain.payment.PriceCalculator
import movie.infrastructure.db.DatabaseConnector
import movie.infrastructure.db.DatabaseInitializer
import movie.infrastructure.db.JdbcMovieRepository
import movie.infrastructure.db.JdbcReservationRepository
import movie.infrastructure.db.JdbcReservedSeatRepository
import movie.infrastructure.db.JdbcScreeningRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.sql.Connection

@Configuration
class AppConfig {
    @Bean
    fun connection(environment: Environment): Connection =
        if (environment.activeProfiles.contains("test")) {
            DatabaseConnector.connectTest()
        } else {
            DatabaseConnector.connectLocal()
        }

    @Bean
    fun databaseInitializer(connection: Connection): DatabaseInitializer = DatabaseInitializer(connection).also { it.initialize() }

    @Bean
    fun movieRepository(
        connection: Connection,
        screeningRepository: JdbcScreeningRepository,
    ): JdbcMovieRepository = JdbcMovieRepository(connection, screeningRepository)

    @Bean
    fun reservedSeatRepository(connection: Connection): JdbcReservedSeatRepository = JdbcReservedSeatRepository(connection)

    @Bean
    fun screeningRepository(
        connection: Connection,
        reservedSeatRepository: JdbcReservedSeatRepository,
    ): JdbcScreeningRepository = JdbcScreeningRepository(connection, reservedSeatRepository)

    @Bean
    fun reservationRepository(
        connection: Connection,
        reservedSeatRepository: JdbcReservedSeatRepository,
    ): JdbcReservationRepository = JdbcReservationRepository(connection, reservedSeatRepository)

    @Bean
    fun priceCalculator(): PriceCalculator =
        PriceCalculator(
            discountPolicies =
                DiscountPolicies(
                    listOf(MovieDayDiscount()),
                    listOf(TimeDiscount()),
                ),
        )
}
