package movie.domain.amount

data class PaymentResult(
    val totalPrice: Price,
    val usedPoint: Point,
)
