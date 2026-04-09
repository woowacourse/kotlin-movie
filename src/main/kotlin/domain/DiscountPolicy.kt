package domain

import java.time.LocalDate
import java.time.LocalTime

class DiscountPolicy {
    companion object {
        fun cardDiscount(given: Money): Money {
            return Money((given.amount * 0.05).toInt())
        }

        fun cashDiscount(given: domain.Money): Money {
            return Money((given.amount * 0.02).toInt())
        }

        fun movieDayDiscount(money: Money, date: LocalDate): Money {
            if (date.dayOfMonth % 10 == 0) return Money((money.amount * 0.1).toInt())

            return Money(0)
        }

        fun timeDiscount(money: Money, time: LocalTime): Money {
            if (time.hour !in 12..<20) return Money(2000)

            return Money(0)
        }
    }

}
