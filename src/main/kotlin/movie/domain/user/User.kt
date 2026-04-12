package movie.domain.user

import movie.domain.amount.Point
import movie.domain.reservation.Reservations

data class User(
    val reservations: Reservations,
    val point: Point,
) {
    fun usablePoint(amount: Int): Point {
        require(amount <= point.value) { "보유 포인트를 초과할 수 없습니다." }
        return Point(amount)
    }
}
