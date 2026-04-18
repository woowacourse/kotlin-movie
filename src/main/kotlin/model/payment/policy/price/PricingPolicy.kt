package model.payment.policy.price

import model.seat.SeatGrade

object PricingPolicy {
    fun price(grade: SeatGrade): Int =
        when (grade) {
            SeatGrade.S -> 18_000
            SeatGrade.A -> 15_000
            SeatGrade.B -> 12_000
        }
}
