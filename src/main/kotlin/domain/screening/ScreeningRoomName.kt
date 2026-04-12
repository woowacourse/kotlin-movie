package domain.screening

@JvmInline
value class ScreeningRoomName(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "이름은 공백일 수 없습니다." }
    }
}
