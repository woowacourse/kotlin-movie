package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.discount.DiscountPolicies
import movie.domain.screening.Screening
import movie.domain.seat.SelectedSeats

class Reservation(
    private val screening: Screening,
    private val selectedSeats: SelectedSeats,
) {
    fun isTimeOverlapping(other: Reservation): Boolean =
        screening.isTimeOverlapping(other.screening)

    fun calculateDiscountedPrice(discountPolicies: DiscountPolicies): Money =
        discountPolicies.applyDiscount(
            selectedSeats.totalPrice,
            screening.screeningDateTime,
        )

    fun display(): String =
        "- [${screening.title}] ${screening.screeningDateTime.date} ${screening.screeningDateTime.startTime}  좌석: ${selectedSeats.display()}"
}
