package movie.domain.user

import movie.domain.amount.Point
import movie.domain.reservation.ReservationData

import movie.domain.reservation.Reservations
import org.assertj.core.api.Assertions.assertThat
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
        assertThat(user.reservations).isEqualTo(reservations)
        assertThat(user.point).isEqualTo(point)
    }
}
