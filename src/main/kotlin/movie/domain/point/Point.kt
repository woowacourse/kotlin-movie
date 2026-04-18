package movie.domain.point

import movie.error.PaymentErrorMessage

@JvmInline
value class Point(
    val amount: Int,
) {
    init {
        require(amount >= 0) { PaymentErrorMessage.INVALID_POINT }
    }

    constructor(input: String) : this(
        amount = input.toIntOrNull() ?: throw IllegalArgumentException(PaymentErrorMessage.POINT_NOT_INTEGER),
    )
}
