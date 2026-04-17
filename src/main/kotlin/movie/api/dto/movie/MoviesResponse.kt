package movie.api.dto.movie

import movie.domain.movie.Movie

data class MoviesResponse(
    val movies: List<MovieResponse>,
) {
    companion object {
        fun from(
            movies: List<Movie>,
            runningTimeMap: Map<Long, Int>,
        ): MoviesResponse =
            MoviesResponse(
                movies =
                    movies.map { movie ->
                        MovieResponse.from(movie, runningTimeMap[movie.id] ?: 0)
                    },
            )
    }
}
