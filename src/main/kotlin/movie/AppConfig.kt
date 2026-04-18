package movie

import movie.database.DatabaseFactory
import movie.domain.MovieManager
import movie.domain.PaymentManager
import movie.repository.ReservationRepository
import movie.repository.ScheduleRepository
import movie.service.ReservationService
import movie.service.ScheduleService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun scheduleRepository(): ScheduleRepository = ScheduleRepository(DatabaseFactory.getConnection())

    @Bean
    fun reservationRepository(): ReservationRepository = ReservationRepository(DatabaseFactory.getConnection())

    @Bean
    fun scheduleService(scheduleRepository: ScheduleRepository): ScheduleService {
        return ScheduleService(scheduleRepository)
    }

    @Bean
    fun reservationService(reservationRepository: ReservationRepository): ReservationService {
        return ReservationService(reservationRepository)
    }

    @Bean
    fun paymentManager(): PaymentManager = PaymentManager()

    @Bean
    fun movieManager(scheduleService: ScheduleService): MovieManager {
        return MovieManager(scheduleService.getSchedules())
    }
}
