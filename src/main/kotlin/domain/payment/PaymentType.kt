package domain.payment

enum class PaymentType(
    val discountRate: Double,
) {
    CREDIT_CARD(0.05),
    CASH(0.02),
}
