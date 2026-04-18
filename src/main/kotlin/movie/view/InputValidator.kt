package movie.view

import movie.error.SystemErrorMessage
import movie.error.SeatErrorMessage

object InputValidator {
    private const val DATE_PATTERN = """\d{4}-\d{2}-\d{2}"""
    private const val SEAT_PATTERN = """^[A-Za-z]\d+$"""

    fun validateYesNo(input: String) {
        require(input.uppercase() in setOf("Y", "N")) { SystemErrorMessage.INVALID_YES_NO }
    }

    fun validateDate(input: String) {
        require(Regex(DATE_PATTERN).matches(input)) { SystemErrorMessage.INVALID_DATE_FORMAT }
    }

    fun validateNumber(input: String) {
        requireNotNull(input.toIntOrNull()) { SystemErrorMessage.INVALID_NUMBER_INPUT }
    }

    fun validateSeatNumbers(input: String) {
        val seatInputs = input.split(",").map { it.trim() }

        require(seatInputs.isNotEmpty()) { SeatErrorMessage.EMPTY_INPUT }

        seatInputs.forEach { seat ->
            require(Regex(SEAT_PATTERN).matches(seat)) {
                SeatErrorMessage.INVALID_FORMAT
            }
        }
    }
}
