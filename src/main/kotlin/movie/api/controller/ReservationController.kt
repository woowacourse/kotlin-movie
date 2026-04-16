package movie.api.controller

import movie.api.dto.ReservationRequest
import movie.api.dto.ReservationResponse
import movie.model.Point
import movie.model.payment.Card
import movie.model.payment.Cash
import movie.model.payment.PaymentSystem
import movie.model.policy.DiscountSystem
import movie.model.policy.MovieDayDiscountPolicy
import movie.model.policy.TimeDiscountPolicy
import movie.model.reservation.Reservations
import movie.model.seat.SeatNumber
import movie.repository.inmemory.InMemoryScreenRepository
import movie.repository.jdbc.JdbcMovieRepository
import movie.repository.jdbc.JdbcReservationRepository
import movie.repository.jdbc.JdbcReservedSeatRepository
import movie.repository.jdbc.JdbcScreeningRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class ReservationController {
    val screenRepository = InMemoryScreenRepository
    val movieRepository = JdbcMovieRepository(isLocal = false)
    val screeningRepository =
        JdbcScreeningRepository(
            isLocal = false,
            screenRepository = screenRepository,
            movieRepository = movieRepository,
        )
    val reservationRepository =
        JdbcReservationRepository(isLocal = false, screeningRepository = screeningRepository)
    val reservedSeatRepository = JdbcReservedSeatRepository(isLocal = false)

    @PostMapping("/api/reservations")
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ResponseEntity<ReservationResponse> {
        val reservationList =
            request.reservations.map { reservationDto ->
                val screening =
                    requireNotNull(screeningRepository.findById(reservationDto.screeningId)) { "DB에 찾는 회차 아이디가 없습니다." }

                // Load already reserved seats from DB
                val reservedSeatsInDb = reservedSeatRepository.findSeatsByScreening(screening)
                val updatedScreening =
                    screening.copy(
                        seatMap =
                            movie.model.screening.ScreeningSeatMap(
                                screen = screening.seatMap.screen,
                                reservedSeats = reservedSeatsInDb,
                            ),
                    )

                val seatNumbers =
                    reservationDto.seats.map {
                        SeatNumber(
                            row = it[0],
                            column = it.substring(1).toInt(),
                        )
                    }

                // This will throw IllegalArgumentException if seats are already reserved or duplicate
                updatedScreening.reserve(seatNumbers)
            }

        val currentReservations = Reservations(reservationList)

        val discountPolicies = listOf(MovieDayDiscountPolicy, TimeDiscountPolicy)
        val discountSystem = DiscountSystem(discountPolicies)
        val paymentSystem = PaymentSystem()

        val discountedPrice = discountSystem.discountPrice(currentReservations)
        val paymentMethod =
            when (request.paymentMethod) {
                "CREDIT_CARD" -> Card
                "CASH" -> Cash
                else -> throw IllegalArgumentException("결제 방식 입력이 잘못됐습니다.(ex. CREDIT_CARD)")
            }
        val payResult =
            paymentSystem.pay(
                paymentMethod = paymentMethod,
                discountedPrice = discountedPrice,
                point = Point(request.usedPoints),
            )

        currentReservations.forEach { reservation ->
            reservationRepository.save(reservation)
            reservedSeatRepository.save(reservation.screening, reservation.seats)
        }

        val responseDto =
            ReservationResponse(
                reservationId = id.getAndIncrement(),
                reservations = request.reservations,
                usedPoints = request.usedPoints,
                paymentMethod = request.paymentMethod,
                totalPrice = payResult.finalPrice.value,
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto)
    }

    companion object {
        var id: AtomicLong = AtomicLong(1L)
    }
}
