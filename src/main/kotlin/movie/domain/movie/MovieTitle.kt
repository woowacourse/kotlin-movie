package movie.domain.movie

@JvmInline
value class MovieTitle(
    private val value: String,
) {
    init {
        require(value.isNotBlank()) { "영화 제목은 비어 있을 수 없습니다." }
    }

    fun matches(other: String): Boolean = value == other

    override fun toString(): String = value
}
