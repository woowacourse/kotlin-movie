package model.seat

import model.seat.SeatRank

data class SeatInventory(
    val seats: List<Seat>,
) {
    init {
        require(seats.isNotEmpty()) { "좌석 정보가 없습니다" }
        require(seats.all { it.row in rows && it.column in columns }) { "존재하지 않는 좌석입니다" }
    }

    // 좌석을 찾는 함수
    fun findSeat(seatName: String): Seat =
        seats.find { it.getSeatName() == seatName }
            ?: throw IllegalArgumentException("존재하지 않는 좌석입니다")

    // 좌석 예약하는 함수
    fun reserveSeats(seatNames: List<String>): SeatInventory {
        val targetSeats = seatNames.map { findSeat(it) }

        return copy(
            seats =
                seats.map { seat ->
                    if (targetSeats.contains(seat)) {
                        seat.reserve()
                    } else {
                        seat
                    }
                },
        )
    }

    // 좌석의 총액을 계산하는 함수
    fun calculatePrice(seatNames: List<String>): Price =
        Price(
            seatNames.sumOf {
                findSeat(it).seatRank.price.value
            },
        )

    companion object {
        // 세로 범위
        val rows = ('A'..'E').map { it.toString() }

        // 가로 범위
        val columns = (1..4).toList()

        // SeatInventory를 만드는 함수
        fun createDefaultSeatInventory(): SeatInventory =
            SeatInventory(
                seats =
                    rows.flatMap { row ->
                        columns.map { column ->
                            Seat(
                                row = row,
                                column = column,
                                seatRank =
                                    when (row) {
                                        "A", "B" -> SeatRank.B_RANK
                                        "C", "D" -> SeatRank.S_RANK
                                        else -> SeatRank.A_RANK
                                    },
                            )
                        }
                    },
            )
    }
}
