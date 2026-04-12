package domain.discount

import domain.reservation.ReservedScreen

interface DiscountPolicy {
    fun discount(
        reservedScreen: ReservedScreen,
        money: Int,
    ): Int
}
