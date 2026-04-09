package model.payment

import model.Money
import model.Point

data class PayResult(
    val finalPrice: Money,
    val usedPoint: Point
)
