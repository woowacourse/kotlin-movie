package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.screening.Screening

class Reservations(
    private val reservations: List<Reservation>,
) {
    init {
        validateNoOverlapping()
    }

    fun add(reservation: Reservation): List<Reservation> = Reservations(reservations + reservation).reservations

    fun totalPrice(): Money = reservations.map { it.calculatePrice() }.reduce { acc, money -> acc + money }

    fun display(): String {
        return reservations.joinToString("\n") { it.display() }
    }

    fun forEach(action: (Reservation) -> Unit) {
        reservations.forEach(action)
    }

    private fun validateNoOverlapping() {
        reservations.forEachIndexed { index, reservation ->
            val hasOverlap =
                reservations.drop(index + 1).any { other ->
                    reservation.isTimeOverlapping(other.getScreening())
                }

            require(!hasOverlap) { "상영 시간이 겹치는 예매가 존재합니다." }
        }
    }
}
