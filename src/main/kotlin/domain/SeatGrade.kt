package domain

enum class SeatGrade(
    val price: Int,
) {
    S(18000),
    A(15000),
    B(12000),
    ;

    companion object {
        fun of(position: SeatPosition): SeatGrade =
            when (position.row) {
                Row("A"), Row("B") -> B
                Row("C"), Row("D") -> S
                else -> A
            }
    }
}
