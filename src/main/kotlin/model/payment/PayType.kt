package model.payment

private const val CREDIT_CARD_DISCOUNT_RATE = 0.05
private const val CASH_DISCOUNT_RATE = 0.02

enum class PayType(
    val id: Int,
    val discountRate: Double,
) {
    CREDIT_CARD(
        id = 1,
        discountRate = CREDIT_CARD_DISCOUNT_RATE,
    ),
    CASH(
        id = 2,
        discountRate = CASH_DISCOUNT_RATE,
    ),
    ;

    companion object {
        fun fromId(id: Int): PayType =
            entries.firstOrNull { it.id == id }
                ?: throw IllegalArgumentException(Message.INVALID_PAY_TYPE)
    }
}
