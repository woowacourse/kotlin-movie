package model.seat

enum class SeatGrade {
    S,
    A,
    B,
}

fun SeatGrade.toPrice(): Int =
    when (this) {
        SeatGrade.S -> 18_000
        SeatGrade.A -> 15_000
        SeatGrade.B -> 12_000
    }
