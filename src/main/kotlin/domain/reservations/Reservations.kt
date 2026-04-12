package domain.reservations

import domain.dto.ReservationDto
import domain.money.Money
import domain.paycalculator.items.PriceDiscountCalculator
import domain.reservations.items.Reservation
import kotlin.collections.fold

class Reservations(
    private val reservations: MutableList<Reservation> = mutableListOf(),
) {
    fun addReservation(reservation: Reservation) {
        val duplicated = reservations.any { it.isDuplicatedReservation(reservation) }

        require(!duplicated) { "선택하신 상영 시간이 겹칩니다." }

        reservations.add(reservation)
    }

    fun calculateTotalDiscountPrice(priceDiscountCalculator: PriceDiscountCalculator): Money {
        val price = Money(0)
        return reservations.fold(price) { total, reservation ->
            total + reservation.calculateDiscountPrice(priceDiscountCalculator)
        }
    }

    fun toReservationDtoList(): List<ReservationDto> = reservations.map { it.toDto() }
}
