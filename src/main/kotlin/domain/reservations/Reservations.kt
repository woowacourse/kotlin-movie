package domain.reservations

import domain.reservations.items.Reservation
import java.time.LocalDate
import java.time.LocalTime

class Reservations {
    private val _reservations = mutableListOf<Reservation>()

    val reservations get() = _reservations.toList()

    fun addReservation(reservation: Reservation) {
        _reservations.add(reservation)
    }

    fun checkDuplicateTime(inputTime: LocalTime): Boolean =
        _reservations.any {
            it.isDuplicatedTime(inputTime)
        }

    fun checkDuplicateDate(inputDate: LocalDate): Boolean =
        _reservations.any {
            it.isDuplicatedDate(inputDate)
        }
}
