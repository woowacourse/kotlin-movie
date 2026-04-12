package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point

data class PaymentResult(
    val totalPrice: Money,
    val usedPoint: Point,
)
