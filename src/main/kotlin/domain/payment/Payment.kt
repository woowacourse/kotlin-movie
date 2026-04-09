package domain.payment

import domain.amount.Money
import domain.amount.Point

data class Payment(
    val price: Money,
    val usagePoint: Point,
)
