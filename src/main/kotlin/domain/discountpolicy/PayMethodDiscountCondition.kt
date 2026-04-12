package domain.discountpolicy

interface PayMethodDiscountCondition {
    fun isSatisfiedBy(payMethod: PayMethod): Boolean
}

class CardCondition : PayMethodDiscountCondition {
    override fun isSatisfiedBy(payMethod: PayMethod): Boolean = payMethod == PayMethod.CARD
}

class CashCondition : PayMethodDiscountCondition {
    override fun isSatisfiedBy(payMethod: PayMethod): Boolean = payMethod == PayMethod.CASH
}

enum class PayMethod {
    CARD,
    CASH,
}
