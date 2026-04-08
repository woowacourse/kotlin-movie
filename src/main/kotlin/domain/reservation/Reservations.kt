package domain.reservation

import domain.amount.Money

class Reservations(
    private val reservations: List<Reservation>,
) {
    init {
        require(reservations.isNotEmpty()) { "예매 목록은 비어 있을 수 없습니다." }
        validateNoOverlapping()
    }

    fun add(reservation: Reservation): List<Reservation> = Reservations(reservations + reservation).reservations

    fun totalPrice(): Money = reservations.map { it.calculatePrice() }.reduce { acc, money -> acc + money }

    private fun validateNoOverlapping() {
        reservations.forEachIndexed { index, reservation ->
            val hasOverlap =
                reservations.drop(index + 1).any {
                    reservation.isTimeOverlapping(it)
                }
            require(!hasOverlap) { "상영 시간이 겹치는 예매가 존재합니다." }
        }
    }
}
