package movie.domain.payment

import movie.domain.amount.Money
import movie.domain.amount.Point

data class Payment(
    val price: Money,
    val usagePoint: Point,
)
