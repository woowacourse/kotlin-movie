package domain.user

import domain.amount.Point
import domain.reservation.ReservationData
import domain.reservation.Reservations
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `예매 목록과 포인트를 갖고 있다`() {
        // given
        val reservations = Reservations(ReservationData.reservations)
        val point = Point(1000)

        // when
        val user = User(reservations, point)

        // then
        assert(user.reservations == reservations)
        assert(user.point == point)
    }
}
