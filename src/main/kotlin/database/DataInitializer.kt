package database

import database.repository.MovieRepository
import database.repository.MovieScreeningRepository

object DataInitializer {
    fun initialize() {
        MovieRepository().save()
        MovieScreeningRepository().save()
    }
}
