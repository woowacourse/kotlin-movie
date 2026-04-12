package domain.payment

import domain.common.Money
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DiscountPolicy {
    fun calculateDiscountResult(
        money: Money,
        point: Point,
        dateTime: LocalDateTime,
        paymentType: PaymentType,
    ): Money {
        var result = money
        result -= movieDayDiscount(result, dateTime.toLocalDate())
        result -= timeDiscount(result, dateTime.toLocalTime())
        result -= point.amount
        result -= paymentDiscount(result, paymentType)

        return result
    }

    fun movieDayDiscount(
        money: Money,
        date: LocalDate,
    ): Money {
        if (date.dayOfMonth % 10 == 0) return Money((money.amount * 0.1).toInt())

        return Money(0)
    }

    fun timeDiscount(
        money: Money,
        time: LocalTime,
    ): Money {
        if (time.hour !in 12..<20) return Money(2000)

        return Money(0)
    }

    fun paymentDiscount(
        money: Money,
        paymentType: PaymentType,
    ): Money = money * paymentType.discountRate
}
