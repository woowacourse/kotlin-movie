package domain.reservations.items

import domain.money.Money
import domain.movie.Movie
import domain.seat.Seat
import domain.timetable.items.ScreenTime
import java.time.LocalDate
import java.time.LocalTime

class Reservation(
    private val movie: Movie,
    private val screenTime: ScreenTime,
    private val seats: List<Seat>,
) {
    fun isDuplicatedDate(date: LocalDate): Boolean = screenTime.isScreeningAt(date)

    fun isDuplicatedTime(time: LocalTime): Boolean = screenTime.isContain(time)

    fun getReservationInfo(): ReservationInfo {
        val money = Money(0)
        val price =
            seats.fold(money) { total, money ->
                total + money.addSeatPrice(total)
            }

        return ReservationInfo(
            screenTime = screenTime,
            price = price,
        )
    }
}
