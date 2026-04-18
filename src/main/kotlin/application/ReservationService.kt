package application

import api.dto.reservation.CreateReservationRequest
import api.dto.reservation.CreateReservationResponse
import api.dto.reservation.ReservationItemResponse
import domain.cinema.MovieTheater
import domain.cinema.ScreeningSchedule
import domain.reservation.Cart
import domain.seat.Seat
import domain.seat.SeatCoordinate
import domain.user.User
import org.springframework.stereotype.Service
import persistence.CinemaDatabase
import persistence.ScreeningIdGenerator
import util.ErrorMessage
import java.util.UUID

@Service
class ReservationService(
    private val cinemaDatabase: CinemaDatabase,
    private val apiUser: User,
) {
    fun create(request: CreateReservationRequest): CreateReservationResponse {
        validateRequest(request)

        val movieTheater = cinemaDatabase.loadMovieTheater()
        val cart = buildCart(movieTheater, request)
        require(request.usedPoints <= cart.totalPrice()) { ErrorMessage.INVALID_POINT_INPUT }

        val reservationId = UUID.randomUUID().toString()
        val paymentMethod = ApiPaymentMethod.from(request.paymentMethod)
        var receipt = cart.issueReceipt()
        receipt = receipt.applyPoint(apiUser, request.usedPoints.toString())
        receipt = receipt.applyPaymentMethod(paymentMethod.toDomain())
        receipt.confirm(apiUser)
        cinemaDatabase.save(reservationId, receipt)

        return CreateReservationResponse(
            reservationId = reservationId,
            reservations =
                request.reservations.map { item ->
                    ReservationItemResponse(
                        screeningId = item.screeningId,
                        seats = item.seats,
                    )
                },
            usedPoints = receipt.usedPoint,
            paymentMethod = paymentMethod.value,
            totalPrice = receipt.totalPrice(),
        )
    }

    private fun validateRequest(request: CreateReservationRequest) {
        require(request.reservations.isNotEmpty()) { ErrorMessage.INVALID_INPUT }
        require(request.usedPoints >= 0) { ErrorMessage.INVALID_POINT_INPUT }

        request.reservations.forEach { item ->
            require(item.screeningId.isNotBlank()) { ErrorMessage.SCREENING_NOT_FOUND }
            require(item.seats.isNotEmpty()) { ErrorMessage.INVALID_SEAT_INPUT }
        }
    }

    private fun buildCart(
        movieTheater: MovieTheater,
        request: CreateReservationRequest,
    ): Cart =
        request.reservations
            .groupBy { it.screeningId }
            .mapValues { (_, items) -> items.flatMap { it.seats } }
            .entries
            .fold(Cart(emptyList())) { cart, (screeningId, seatInputs) ->
                val screening = findScreening(movieTheater, screeningId)
                cart.checkReservationHistory(screening)
                val seats = findSeats(screening, seatInputs)
                cart.addAll(screening, seats)
            }

    private fun findScreening(
        movieTheater: MovieTheater,
        screeningId: String,
    ): ScreeningSchedule =
        movieTheater.screenings.find { ScreeningIdGenerator.generate(it) == screeningId }
            ?: throw IllegalArgumentException(ErrorMessage.SCREENING_NOT_FOUND)

    private fun findSeats(
        screening: ScreeningSchedule,
        seatInputs: List<String>,
    ): List<Seat> {
        val coordinates = seatInputs.map(::parseSeatCoordinate)
        require(coordinates.distinctBy { "${it.row}${it.column}" }.size == coordinates.size) {
            ErrorMessage.INVALID_SEAT_INPUT
        }

        return screening.screen.selectSeats(coordinates)
    }

    private fun parseSeatCoordinate(input: String): SeatCoordinate {
        require(seatPattern.matches(input)) { ErrorMessage.INVALID_SEAT_INPUT }

        val row = input.first()
        val column = input.substring(1).toInt()
        return SeatCoordinate(row, column)
    }

    companion object {
        private val seatPattern = Regex("^[A-Z][0-9]+$")
    }
}
