package domain.paycalculator.items

import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import domain.timetable.items.ScreenTime

class PriceDiscountCalculator(
    private val policies: List<TimeDiscountPolicy>,
) {
    fun calculate(
        price: Money,
        screenTime: ScreenTime,
    ): Money =
        policies.fold(price) { totalPrice, policy ->
            policy.applyDiscount(totalPrice, screenTime)
        }
}
