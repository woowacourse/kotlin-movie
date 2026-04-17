package movie.data.db

import movie.data.MovieData
import movie.data.db.movie.MovieRepository
import movie.data.db.screening.ScreeningRepository

object DatabaseInitializer {
    fun initialize() {
        DatabaseManager.connection.use { connection ->
            SchemaInitializer.initialize(connection)

            val movieRepository = MovieRepository(connection)
            val screeningRepository = ScreeningRepository(connection)
            val movies = MovieData.createMovies()

            if (movieRepository.isEmpty()) {
                movies.forEach { movie ->
                    movieRepository.save(movie, 120)

                    movie.screenings.screenings.forEach { screening ->
                        screeningRepository.save(movie.id, screening)
                    }
                }
            }
        }
    }
}
