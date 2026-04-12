package domain.seat

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
                Row("E") -> A
                else -> throw IllegalArgumentException("존재하지 않는 좌석입니다.")
            }
    }
}
