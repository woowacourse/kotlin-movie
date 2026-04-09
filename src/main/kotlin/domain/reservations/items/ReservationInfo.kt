package domain.reservations.items

import domain.money.Money
import domain.timetable.items.ScreenTime

data class ReservationInfo(
    val screenTime: ScreenTime,
    val price: Money,
)
