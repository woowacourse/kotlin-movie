package api

import api.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import util.ErrorMessage

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val message = exception.message ?: ErrorMessage.INVALID_INPUT
        return ResponseEntity.status(statusOf(message)).body(ErrorResponse(message))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse(ErrorMessage.INVALID_INPUT))

    @ExceptionHandler(Exception::class)
    fun handleException(): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(ErrorMessage.INVALID_INPUT))

    private fun statusOf(message: String): HttpStatus =
        when (message) {
            ErrorMessage.SCREENING_NOT_FOUND -> HttpStatus.NOT_FOUND
            ErrorMessage.SEAT_ALREADY_RESERVED,
            ErrorMessage.OVERLAPPING_SCREENING,
            -> HttpStatus.CONFLICT
            else -> HttpStatus.BAD_REQUEST
        }
}
