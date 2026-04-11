package domain.reservations

import domain.reservations.items.Reservation
import domain.timetable.items.ScreenTime

class Reservations {
    private val _reservations = mutableListOf<Reservation>()

    val reservations get() = _reservations.toList()

    fun addReservation(reservation: Reservation) {
        _reservations.add(reservation)
    }

    fun checkDuplicate(screenTime: ScreenTime): Boolean {
        val startTime = screenTime.getStartTime()
        val screeningDate = screenTime.getScreeningDate()

        return _reservations.any {
            it.isDuplicatedDate(screeningDate) && it.isDuplicatedTime(startTime)
        }
    }
}
