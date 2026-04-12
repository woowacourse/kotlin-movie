package movie.domain.screening

import movie.domain.seat.Seat
import java.time.LocalDate
import java.time.LocalTime

class ScreeningSlot(
    val screen: Screen,
    val screeningDateTime: ScreeningDateTime,
) {
    val date: LocalDate
        get() = screeningDateTime.date

    val startTime: LocalTime
        get() = screeningDateTime.startTime

    fun isOverlapping(other: ScreeningSlot): Boolean = screeningDateTime.isOverlapping(other.screeningDateTime)

    fun hasSeat(seat: Seat): Boolean = screen.seats.hasSeat(seat)
}

