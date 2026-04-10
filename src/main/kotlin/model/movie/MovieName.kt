package model.movie

@JvmInline
value class MovieName(
    val name: String,
) {
    override fun toString(): String = name
}
