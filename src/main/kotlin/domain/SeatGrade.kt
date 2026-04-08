package domain

enum class SeatGrade(
    val price: Int,
) {
    S(18000),
    A(15000),
    B(12000),
    ;

    companion object {
        fun of(
            row: Row,
            column: Column,
        ): SeatGrade =
            when (row) {
                Row("A"), Row("B") -> B
                Row("C"), Row("D") -> S
                else -> A
            }
    }
}
