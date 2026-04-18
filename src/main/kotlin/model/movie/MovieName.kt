package model.movie

import Message.EMPTY_MOVIE_NAME

@JvmInline
value class MovieName(
    val name: String,
) {
    init {
        require(name.isNotBlank()) { EMPTY_MOVIE_NAME }
    }

    override fun toString(): String = name
}
