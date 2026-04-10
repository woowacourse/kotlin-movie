package view

enum class YesNo {
    Y,
    N,
    ;

    fun isYes(): Boolean = this == Y

    companion object {
        fun from(input: String): YesNo = entries.firstOrNull { it.name == input }
            ?: throw IllegalArgumentException("입력값은 Y 혹은 N이어야 합니다.")
    }
}
