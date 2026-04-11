package domain.model.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SeatInventoryTest {
    @Test
    fun `한 상영관의 여러 좌석 중 target 좌석이 예약이 된다면 true를 반환한다`() {
        val seatInventory = SeatInventory(seats = SeatInventory.defaultSeatAvailabilities())
        val targetSeat = Seat(3, RowLabel.A)

        val result = seatInventory.isAvailable(targetSeat)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `한 상영관의 여러 좌석 중 target 좌석이 예약이 된다면 예약 상태가 변경된다`() {
        val seatInventory = SeatInventory(seats = SeatInventory.defaultSeatAvailabilities())
        val targetSeat = Seat(3, RowLabel.A)

        val reserveSeatCount = seatInventory.reserve(targetSeat)
        val result = reserveSeatCount.isAvailable(targetSeat)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `한 상영관의 여러 좌석 중 target 좌석이 예약이 안된다면 오류가 발생한다`() {
        val seatInventory = SeatInventory(seats = SeatInventory.defaultSeatAvailabilities())
        val targetSeat = Seat(3, RowLabel.A)

        val reserveSeatCount = seatInventory.reserve(targetSeat)

        assertThrows(IllegalArgumentException::class.java) {
            reserveSeatCount.reserve(targetSeat)
        }
    }
}
