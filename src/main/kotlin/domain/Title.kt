package domain

@JvmInline
value class Title(
    val title: String,
) {
    init {
        require(title.isNotBlank()) {
            "제목은 공백일 수 없습니다."
        }
    }

    override fun toString(): String = title
}
