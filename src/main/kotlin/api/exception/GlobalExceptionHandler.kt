package api.exception

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(
    val message: String,
)

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ScreeningNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleScreeningNotFound(e: ScreeningNotFoundException): ErrorResponse = ErrorResponse(e.message ?: "상영 정보를 찾을 수 없습니다.")

    @ExceptionHandler(SeatAlreadyReservedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleSeatAlreadyReserved(e: SeatAlreadyReservedException): ErrorResponse = ErrorResponse(e.message ?: "이미 예약된 좌석입니다.")

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(e: HttpMessageNotReadableException): ErrorResponse = ErrorResponse("잘못된 요청 형식입니다.")
}
