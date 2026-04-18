package movie.domain

import movie.error.MovieErrorMessage

@JvmInline
value class MovieTitle(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { MovieErrorMessage.BLANK_TITLE }
    }
}
