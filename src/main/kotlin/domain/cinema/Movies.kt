package domain.cinema

import domain.Id

class Movies(val movies: List<Movie>) {
    fun findMovieById(id: Id): Movie {
        val movieIndex = movies.indexOfFirst { it.id.value == id.value }
        require(movieIndex != -1) { "해당 영화는 존재하지 않습니다." }
        return movies[movieIndex]
    }

    fun findMovieByTitle(title: String): Movie {
        val movieIndex = movies.indexOfFirst { it.title == title }
        require(movieIndex != -1) { "존재하지 않는 영화입니다." }
        return movies[movieIndex]
    }
}
