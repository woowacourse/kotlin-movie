package domain.payment

import domain.amount.Money

data class Payment(
    val price: Money,
    val usagePoint: Money,
)
