package model.movie

@JvmInline
value class MovieName(
    private val name: String,
) {
    override fun toString(): String = name
}
