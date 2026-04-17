package movie.api.controller.reservation

import movie.api.dto.reservation.ReservationItemResponse
import movie.api.dto.reservation.ReservationOrderResponse
import movie.api.dto.reservation.ReservationRequest
import movie.data.db.DatabaseManager
import movie.data.db.reservation.ReservationOrderRepository
import movie.data.db.screening.ScreeningRepository
import movie.domain.amount.Point
import movie.domain.discount.DiscountPolicyAdapter
import movie.domain.discount.MovieDayDiscount
import movie.domain.discount.PaymentMethodDiscountPolicy
import movie.domain.discount.TimeDiscount
import movie.domain.movie.Movie
import movie.domain.payment.PaymentMethod
import movie.domain.payment.PriceCalculator
import movie.domain.reservation.Reservation
import movie.domain.reservation.Reservations
import movie.domain.screening.Screenings
import movie.domain.seat.Seats
import movie.domain.seat.SelectedSeats
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ReservationApiController {
    @PostMapping("/reservations")
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ResponseEntity<ReservationOrderResponse> =
        DatabaseManager.connection.use { connection ->
            val screeningRepository = ScreeningRepository(connection)
            val reservationOrderRepository = ReservationOrderRepository(connection)

            // 결제 수단 파싱
            val paymentMethod = parsePaymentMethod(request.paymentMethod)

            // 요청
            val reservations = toReservations(request, screeningRepository)

            // 가격 계산
            val point = Point(request.usedPoints)
            val priceCalculator = PriceCalculator()
            val discountPolicy =
                DiscountPolicyAdapter(
                    percentagePolicies = listOf(MovieDayDiscount()),
                    fixedPolicies = listOf(TimeDiscount()),
                )
            val paymentDiscountPolicy = PaymentMethodDiscountPolicy()

            val paymentResult =
                priceCalculator.calculate(
                    reservations,
                    discountPolicy,
                    paymentDiscountPolicy,
                    point,
                    paymentMethod,
                )

            // DB 저장
            val orderId = reservationOrderRepository.save(reservations, paymentResult, paymentMethod)

            // 응답 생성
            val response =
                ReservationOrderResponse(
                    reservationId = orderId,
                    reservations =
                        request.reservations.map { item ->
                            ReservationItemResponse(
                                screeningId = item.screeningId,
                                seats = item.seats,
                            )
                        },
                    usedPoints = paymentResult.usedPoint.value,
                    paymentMethod = request.paymentMethod,
                    totalPrice = paymentResult.totalPrice.value,
                )

            ResponseEntity.status(HttpStatus.CREATED).body(response)
        }

    private fun toReservations(
        request: ReservationRequest,
        screeningRepository: ScreeningRepository,
    ): Reservations {
        val reservationList =
            request.reservations.map { item ->
                // 상영 조회 (없으면 예외)
                val screening =
                    screeningRepository.findById(item.screeningId)
                        ?: throw IllegalArgumentException("존재하지 않는 상영입니다: ${item.screeningId}")

                // 좌석을 Seat 객체로 변환
                val seats =
                    item.seats
                        .map { seatStr ->
                            val row = seatStr.substring(0, 1).uppercase()
                            val column = requireNotNull(seatStr.substring(1).toInt()) { "유효하지 않은 좌석 형식입니다: $seatStr" }
                            screening.screen.seats.findSeat(row, column)
                        }.toSet()

                // 예약된 좌석인지 검증
                val seatsDomain = Seats(seats)
                val updatedScreening = screening.reserve(seatsDomain)

                val movie = Movie(id = 0, title = "", screenings = Screenings(emptyList()))

                Reservation(movie, updatedScreening, SelectedSeats(seatsDomain))
            }

        return Reservations(reservationList)
    }

    private fun parsePaymentMethod(value: String): PaymentMethod =
        when (value.uppercase()) {
            "CREDIT_CARD" -> PaymentMethod.CreditCard
            "CASH" -> PaymentMethod.Cash
            else -> throw IllegalArgumentException("유효하지 않은 결제 수단입니다: $value")
        }
}
