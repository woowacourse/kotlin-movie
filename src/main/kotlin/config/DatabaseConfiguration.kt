package config

import domain.Id
import domain.user.User
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import persistence.CinemaDatabase

@Configuration
class DatabaseConfiguration {
    @Bean
    @ConditionalOnMissingBean(CinemaDatabase::class)
    fun cinemaDatabase(): CinemaDatabase = CinemaDatabase.local()

    @Bean
    @ConditionalOnMissingBean(User::class)
    fun apiUser(): User = User(Id("user-api"))
}
