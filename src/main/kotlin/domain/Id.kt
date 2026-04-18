package domain

import util.ErrorMessage

data class Id(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { ErrorMessage.ID_MUST_NOT_BE_BLANK }
    }
}
