package api.dto

import model.discount.PaymentMethod

data class ReservationRequest(
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: PaymentMethod,
)

data class ReservationItemRequest(
    val screeningId: Long,
    val seats: List<String>,
)

data class ReservationResponse(
    val reservationId: Long,
    val reservations: List<ReservationItemRequest>,
    val usedPoints: Int,
    val paymentMethod: PaymentMethod,
    val totalPrice: Int,
)
