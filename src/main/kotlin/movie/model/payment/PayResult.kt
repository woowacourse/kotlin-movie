package movie.model.payment

import movie.model.Money
import movie.model.Point

data class PayResult(
    val finalPrice: Money,
    val usedPoint: Point,
)
