package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.discount.DiscountPolicies

data class Reservations(
    private val reservations: List<Reservation>,
) {
    init {
        validateNoOverlapping()
    }

    fun add(reservation: Reservation): Reservations = Reservations(reservations + reservation)

    fun totalPrice(discountPolicies: DiscountPolicies): Money =
        reservations.fold(Money(0)) { acc, reservation ->
            acc + reservation.calculateDiscountedPrice(discountPolicies)
        }

    fun display(): String = reservations.joinToString("\n") { it.display() }


    private fun validateNoOverlapping() {
        reservations.forEachIndexed { index, reservation ->
            val hasOverlap =
                reservations.drop(index + 1).any { other ->
                    reservation.isTimeOverlapping(other)
                }

            require(!hasOverlap) { "상영 시간이 겹치는 예매가 존재합니다." }
        }
    }
}
