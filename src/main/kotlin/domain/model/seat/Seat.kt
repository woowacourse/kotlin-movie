package domain.model.seat

// 좌석은 row(알파벳) + column(숫자) + 좌석 등급으로 구성된다.
data class Seat(
    val column: Int,
    val row: RowLabel,
    val seatClass: SeatClass = SeatClass.from(row),
) {
    init {
        require(column in columns) { "존재하지 않은 좌석입니다." }
        require(row in rows) { "존재하지 않은 좌석입니다." }
    }

    companion object {
        // 좌석 번호 범위: 1 ~ 12
        val columns = (1..12).toList()

        // 좌석 행 범위: A ~ E
        val rows = RowLabel.entries
    }
}

// 좌석 등급
enum class SeatClass {
    B,
    A,
    S, ;

    companion object {
        // 행 기준으로 좌석 등급을 결정한다.
        fun from(row: RowLabel): SeatClass =
            when (row) {
                RowLabel.A, RowLabel.B -> B
                RowLabel.C -> A
                RowLabel.D, RowLabel.E -> S
            }

        // 좌석 등급의 가격을 반환한다.
        fun toPrice(seatClass: SeatClass): Int =
            when (seatClass) {
                B -> 12000
                A -> 15000
                S -> 18000
            }
    }
}

// 좌석 열(A~E)
enum class RowLabel {
    A,
    B,
    C,
    D,
    E,
}
