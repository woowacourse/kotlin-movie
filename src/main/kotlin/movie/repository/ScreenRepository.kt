package movie.repository

import movie.model.Screen

interface ScreenRepository {
    fun findByName(name: String): Screen?
}
