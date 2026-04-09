package model.movie

data class MovieName(
    val name: String,
    private val id: String,
) {
    override fun toString(): String = name
}
