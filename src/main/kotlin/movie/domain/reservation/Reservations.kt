package movie.domain.reservation

import movie.domain.amount.Price
import movie.domain.discount.DiscountPolicies

class Reservations(
    private val reservations: List<Reservation>,
) {
    init {
        validateNoOverlapping()
    }

    fun add(reservation: Reservation): List<Reservation> = Reservations(reservations + reservation).reservations

    fun discountedTotalPrice(discountPolicies: DiscountPolicies): Price {
        var price = Price(0)
        reservations.forEach { reservation ->
            val screeningDateTime = reservation.screening.screeningDateTime
            val discounted =
                discountPolicies.applyDiscount(
                    reservation.calculatePrice(),
                    screeningDateTime.date.atTime(screeningDateTime.startTime),
                )
            price += discounted
        }
        return price
    }

    fun getReservations(): List<Reservation> = reservations

    private fun validateNoOverlapping() {
        reservations.forEachIndexed { index, reservation ->
            val hasOverlap =
                reservations.drop(index + 1).any { other ->
                    reservation.isTimeOverlapping(other.screening)
                }

            require(!hasOverlap) { "상영 시간이 겹치는 예매가 존재합니다." }
        }
    }
}
