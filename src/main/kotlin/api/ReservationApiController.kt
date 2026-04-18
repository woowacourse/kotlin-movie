package api

import api.dto.reservation.CreateReservationRequest
import api.dto.reservation.CreateReservationResponse
import application.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reservations")
class ReservationApiController(
    private val reservationService: ReservationService,
) {
    @PostMapping
    fun createReservation(
        @RequestBody request: CreateReservationRequest,
    ): ResponseEntity<CreateReservationResponse> =
        ResponseEntity
            .status(HttpStatus.CREATED)
            .body(reservationService.create(request))
}
