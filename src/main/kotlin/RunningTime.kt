@JvmInline
value class RunningTime(
    val minute: Int,
) {
    init {
        require(minute > 0)
    }
}
