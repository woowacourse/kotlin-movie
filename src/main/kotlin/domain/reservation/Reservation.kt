package domain.reservation

import domain.common.Money
import domain.payment.PaymentType
import domain.ticket.Ticket

data class Reservation(
    val id: Long? = null,
    val tickets: List<Ticket>,
    val usedPoints: Money,
    val paymentMethod: PaymentType,
    val totalPrice: Money
)
