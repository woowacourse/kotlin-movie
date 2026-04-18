package movie.api

import movie.error.PaymentErrorMessage
import movie.error.SeatErrorMessage
import movie.domain.*
import movie.domain.payment.Card
import movie.domain.payment.Cash
import movie.domain.point.Point
import movie.domain.seat.SeatNumber
import movie.dto.api.request.ReservationRequest
import movie.dto.api.response.*
import movie.repository.ScheduleRepository
import movie.service.ReservationService
import movie.service.ScheduleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class MovieApiController(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleService: ScheduleService,
    private val reservationService: ReservationService,
    private val paymentManager: PaymentManager
) {
    @GetMapping("/movies")
    fun getMovies(): MovieResponse {
        val schedules = scheduleRepository.findAllSchedule()
        val movies = schedules.groupBy { it.movieId }.map { (movieId, movieSchedules) ->
            val first = movieSchedules.first()
            MovieDto(
                id = movieId,
                title = first.title,
                runningTimeMinutes = first.runningTime,
                screenings = movieSchedules.map {
                    ScreeningDto(
                        id = it.scheduleId,
                        startAt = it.startTime,
                        endAt = it.endTime
                    )
                }
            )
        }
        return MovieResponse(movies)
    }

    @PostMapping("/reservations")
    fun createReservation(@RequestBody request: ReservationRequest): ResponseEntity<Any> {
        return try {
            val cart = Cart()
            val allSchedules = scheduleService.getSchedules()

            request.reservations.forEach { item ->
                val schedule = allSchedules.getScheduleById(item.screeningId)
                val seats = item.seats.map { SeatNumber(it) }

                require(!schedule.isReservationSeats(seats)) { SeatErrorMessage.ALREADY_RESERVED }

                schedule.addSeats(seats)
                cart.addReservation(schedule, seats)
            }

            val paymentMethod = when (request.paymentMethod) {
                "CREDIT_CARD" -> Card()
                "CASH" -> Cash()
                else -> throw IllegalArgumentException(PaymentErrorMessage.INVALID_METHOD)
            }

            val finalPrice = paymentManager.calculateFinalPrice(
                cart = cart,
                usePoint = Point(request.usedPoints),
                paymentMethod = paymentMethod
            )

            var firstReservationId: Long = 0
            cart.getReservations().forEachIndexed { index, reservation ->
                val id = reservationService.saveReservation(reservation)
                if (index == 0) firstReservationId = id
            }

            ResponseEntity.status(HttpStatus.CREATED).body(
                ReservationResponse(
                    reservationId = firstReservationId,
                    totalPrice = finalPrice.amount
                )
            )
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to (e.message ?: "Internal Server Error")))
        }
    }
}
