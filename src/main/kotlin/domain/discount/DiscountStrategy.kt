package domain.discount

import domain.common.Money
import domain.payment.PaymentType
import java.time.LocalDateTime

interface TicketDiscountStrategy {
    fun apply(money: Money, context: TicketDiscountContext): Money
}
interface TotalDiscountStrategy {
    fun apply(money: Money, context: PaymentDiscountContext): Money
}

data class TicketDiscountContext(val dateTime: LocalDateTime)

data class PaymentDiscountContext(val paymentType: PaymentType)
