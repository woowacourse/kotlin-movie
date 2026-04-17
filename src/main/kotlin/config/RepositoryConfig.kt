package config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import repository.MovieRepository
import repository.ReservationRepository
import repository.ScreeningRepository

@Configuration
class RepositoryConfig {
    @Bean
    fun movieRepository() = MovieRepository()

    @Bean
    fun screeningRepository() = ScreeningRepository()

    @Bean
    fun reservationRepository(screeningRepository: ScreeningRepository) = ReservationRepository(screeningRepository)
}
