package domain.discountpolicy

import domain.money.Money

interface PayMethodDiscountPolicy {
    fun applyDiscount(
        price: Money,
        payMethod: PayMethod,
    ): Money
}

class CardDiscountPolicy(
    private val payMethodDiscountCondition: PayMethodDiscountCondition,
) : PayMethodDiscountPolicy {
    override fun applyDiscount(
        price: Money,
        payMethod: PayMethod,
    ): Money {
        if (payMethodDiscountCondition.isSatisfiedBy(payMethod)) return price * CARD_DISCOUNT
        return price
    }

    companion object {
        private const val CARD_DISCOUNT = 0.95
    }
}

class CashDiscountPolicy(
    private val payMethodDiscountCondition: PayMethodDiscountCondition,
) : PayMethodDiscountPolicy {
    override fun applyDiscount(
        price: Money,
        payMethod: PayMethod,
    ): Money {
        if (payMethodDiscountCondition.isSatisfiedBy(payMethod)) return price * CASH_DISCOUNT
        return price
    }

    companion object {
        private const val CASH_DISCOUNT = 0.98
    }
}
