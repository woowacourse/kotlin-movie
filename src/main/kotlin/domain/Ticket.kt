package domain

data class Ticket(
    val screening: Screening,
    val seatPositions: SeatPositions,
) {
    val totalPrice get() = calculate()
    private fun calculate(): Money {
        return seatPositions.calculate()
    }
}
