package http

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class, NoSuchElementException::class)
    fun handleBadRequest(e: RuntimeException): ResponseEntity<String> = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
}
