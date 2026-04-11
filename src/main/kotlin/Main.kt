import controller.Controller
import domain.discountpolicy.CardDiscountPolicy
import domain.discountpolicy.CashDiscountPolicy
import domain.discountpolicy.MovieDayCondition
import domain.discountpolicy.MovieDayDiscountPolicy
import domain.discountpolicy.TimeCondition
import domain.discountpolicy.TimeDiscountPolicy
import domain.money.Money
import view.input.InputView
import view.output.OutputView
import java.time.LocalTime

fun main() {
    val timeCondition =
        TimeCondition(
            beforeTime = LocalTime.of(11, 0),
            afterTime = LocalTime.of(20, 0),
        )
    val timeDiscountPolicy =
        TimeDiscountPolicy(
            condition = timeCondition,
            discountAmount = Money(2000),
        )

    val movieDayCondition =
        MovieDayCondition(
            condition = listOf(10, 20, 30),
        )
    val movieDayDiscountPolicy =
        MovieDayDiscountPolicy(
            condition = movieDayCondition,
            discountRate = 10.0,
        )

    val cardDiscountPolicy = CardDiscountPolicy(discountRate = 5.0)

    val cashDiscountPolicy = CashDiscountPolicy(2.0)

    val discountPolicies = listOf(movieDayDiscountPolicy, timeDiscountPolicy)

    Controller(
        inputView = InputView,
        outputView = OutputView,
        discountPolicies = discountPolicies,
        cardDiscountPolicy = cardDiscountPolicy,
        cashDiscountPolicy = cashDiscountPolicy,
    ).run()
}
