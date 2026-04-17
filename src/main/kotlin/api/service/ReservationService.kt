package api.service

import api.dto.ReservationRequest
import api.dto.ReservationResponse
import model.cart.Cart
import model.cart.CartItem
import model.discount.payDiscountPolicy.PayDiscountBenefits
import model.discount.payDiscountPolicy.PaymentPayDiscountPolicy
import model.discount.payDiscountPolicy.PointPayDiscountPolicy
import model.discount.reserveDiscountPolicy.MovieDayDiscountPolicy
import model.discount.reserveDiscountPolicy.MovieDiscountPolicy
import model.discount.reserveDiscountPolicy.TimeDiscountPolicy
import org.springframework.stereotype.Service
import repository.ReservationRepository
import repository.ScreeningRepository

@Service
class ReservationService(
    private val screeningRepository: ScreeningRepository,
    private val reservationRepository: ReservationRepository,
) {
    fun reserve(request: ReservationRequest): ReservationResponse {
        var cart = Cart()

        request.reservations.forEach { itemRequest ->
            val screening = screeningRepository.findById(itemRequest.screeningId)
            val reservedScreening = screening.reserveSeats(itemRequest.seats)
            cart.parseOverlapping(reservedScreening)
            cart = cart.addItem(CartItem(reservedScreening, itemRequest.seats))
        }

        val moviePrice =
            cart.calculateItemsPrice(
                MovieDiscountPolicy(listOf(MovieDayDiscountPolicy(), TimeDiscountPolicy())),
            )
        val totalPrice =
            PayDiscountBenefits(
                listOf(
                    PointPayDiscountPolicy(request.usedPoints),
                    PaymentPayDiscountPolicy(request.paymentMethod),
                ),
            ).calculatePrice(moviePrice)

        val reservationId =
            reservationRepository.save(
                cart = cart,
                paymentMethod = request.paymentMethod,
                usedPoint = request.usedPoints,
                totalPrice = totalPrice.value,
            )

        return ReservationResponse(
            reservationId = reservationId,
            reservations = request.reservations,
            usedPoints = request.usedPoints,
            paymentMethod = request.paymentMethod,
            totalPrice = totalPrice.value,
        )
    }
}
