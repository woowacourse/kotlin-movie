package domain.paycalculator

import domain.discountpolicy.DiscountPolicy
import domain.discountpolicy.PayMethodDiscountPolicy
import domain.money.Money
import domain.point.Point
import domain.reservations.items.Reservation

class PayCalculator {
    fun calculateInitPrice(reservations: List<Reservation>): Money {
        var initPrice = Money(0)

        for (reservation in reservations) {
            val info = reservation.getReservationInfo()
            initPrice += info.price
        }

        return initPrice
    }

    fun calculateTimeDiscountedPrice(
        price: Money,
        reservations: List<Reservation>,
        discountPolicies: List<DiscountPolicy>,
    ): Money {
        var discountedPrice = price
        for (reservation in reservations) {
            for (discountPolicy in discountPolicies) {
                discountedPrice = discountPolicy.applyDiscount(discountedPrice, reservation)
            }
        }

        return discountedPrice
    }

    fun usePoint(
        price: Money,
        point: Point,
    ): Money {
        if (price < point.exchangeToMoney()) {
            throw IllegalArgumentException("$TOO_MUCH_POINT $price")
        }
        return price - point.exchangeToMoney()
    }

    fun usePayMethod(
        price: Money,
        payMethodDiscountPolicy: PayMethodDiscountPolicy,
    ): Money = payMethodDiscountPolicy.applyDiscount(price)

    companion object {
        const val TOO_MUCH_POINT = "사용할 포인트는 결제할 금액보다 클 수 없습니다. 현재 금액:"
    }
}
