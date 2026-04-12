package movie.domain.user

import movie.domain.amount.Point
import movie.domain.reservation.ReservationData
import movie.domain.reservation.Reservations
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @Test
    fun `보유 포인트를 초과하면 사용할 수 없다`() {
        // given
        val user = User(Reservations(emptyList()), Point(1000))

        // when & then
        assertThatThrownBy { user.usablePoint(1500) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("보유 포인트를 초과할 수 없습니다.")
    }
}
