package domain.discountpolicy

import domain.money.Money

interface PayMethodDiscountPolicy {
    fun applyDiscount(price: Money): Money
}

class CardDiscountPolicy : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * DiscountAmount.CARD_DISCOUNT
}

class CashDiscountPolicy : PayMethodDiscountPolicy {
    override fun applyDiscount(price: Money): Money = price * DiscountAmount.CASH_DISCOUNT
}
