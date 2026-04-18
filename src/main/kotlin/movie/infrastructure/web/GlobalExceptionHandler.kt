package movie.infrastructure.web

import movie.infrastructure.web.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val message = e.message ?: "유효하지 않은 요청입니다."
        return when {
            message.contains("상영 시간이 겹치는") -> {
                ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ErrorResponse(message = message))
            }
            message.contains("상영 정보를 찾을 수 없습니다") -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse(message = message))
            }
            message.contains("이미 예약된 좌석") -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse(message = message))
            }
            message.contains("존재하지 않는") -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse(message = message))
            }
            else -> {
                ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse(message = message))
            }
        }
    }
}
