package domain

@JvmInline
value class Row(
    val value: String,
) {
    init {
        require(value.matches(Regex("[A-Ja-j]"))) {
            "행은 A~J 사이의 알파벳이어야 합니다."
        }
    }
}
