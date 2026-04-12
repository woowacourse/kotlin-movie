package domain.discount

import domain.common.Money
import domain.payment.PaymentType
import java.time.LocalDateTime

interface DiscountStrategy {
    fun apply(money: Money, context: DiscountContext): Money
}

data class DiscountContext(
    val dateTime: LocalDateTime,
    val paymentType: PaymentType
)

