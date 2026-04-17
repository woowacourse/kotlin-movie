package api.controller

import api.dto.ReservationRequest
import api.dto.ReservationResponse
import api.service.ReservationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ReservationApiController(
    private val reservationService: ReservationService,
) {
    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    fun createReservation(
        @RequestBody request: ReservationRequest,
    ): ReservationResponse = reservationService.reserve(request)
}
