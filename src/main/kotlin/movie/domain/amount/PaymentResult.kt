package movie.domain.amount

data class PaymentResult(
    val totalPrice: Money,
    val usedPoint: Point,
)
