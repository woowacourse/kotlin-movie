package movie.domain.reservation

import movie.domain.amount.Money
import movie.domain.discount.DiscountPolicies
import movie.domain.screening.Screening
import movie.domain.screening.ScreeningDateTime
import movie.domain.seat.Seat
import movie.domain.seat.SelectedSeats

class Reservation(
    private val screening: Screening,
    private val selectedSeats: SelectedSeats,
) {
    fun isTimeOverlapping(other: Reservation): Boolean = screening.isTimeOverlapping(other.screening)

    fun screeningId(): Long = screening.id

    fun forEachSeat(action: (String, Int) -> Unit) {
        selectedSeats.forEach { action(it.seatRow.value, it.seatColumn.value) }
    }

    fun seatList(): List<Seat> {
        val seatList = mutableListOf<Seat>()
        selectedSeats.forEach { seatList.add(it) }
        return seatList
    }

    fun calculateDiscountedPrice(discountPolicies: DiscountPolicies): Money =
        discountPolicies.applyDiscount(basePrice(), screeningDateTime())

    fun screeningTitleText(): String = screening.titleText()

    fun screeningDateText(): String = screening.dateText()

    fun screeningStartTimeText(): String = screening.startTimeText()

    fun selectedSeatDisplay(): String = selectedSeats.display()

    private fun basePrice(): Money = selectedSeats.totalPrice

    private fun screeningDateTime(): ScreeningDateTime = screening.screeningDateTime()
}
