package domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class DiscountPolicy {

    fun calculateDiscountResult(
        money: Money,
        point: Point,
        dateTime: LocalDateTime,
        paymentType: PaymentType
    ): Money {
        var result = money
        result -= movieDayDiscount(result, dateTime.toLocalDate())
        result -= timeDiscount(result, dateTime.toLocalTime())
        result -= point.amount
        result -= when (paymentType) {
            PaymentType.CREDIT_CARD -> cardDiscount(result)
            PaymentType.CASH -> cashDiscount(result)
        }

        return result
    }

    fun movieDayDiscount(money: Money, date: LocalDate): Money {
        if (date.dayOfMonth % 10 == 0) return Money((money.amount * 0.1).toInt())

        return Money(0)
    }

    fun timeDiscount(money: Money, time: LocalTime): Money {
        if (time.hour !in 12..<20) return Money(2000)

        return Money(0)
    }

    fun cardDiscount(money: Money): Money {
        return Money((money.amount * 0.05).toInt())
    }

    fun cashDiscount(money: Money): Money {
        return Money((money.amount * 0.02).toInt())
    }
}
