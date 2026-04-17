package model.movie

class Movies(
    _value: List<Movie>,
) {
    init {
        require(_value.isNotEmpty()) { "영화 정보가 없습니다" }
    }

    val value = _value.toList()

    fun getMovie(movieTitle: String): Movie =
        value.find { it.title == movieTitle }
            ?: throw IllegalArgumentException("존재하지 않는 영화입니다")
}
