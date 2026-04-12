package model.seat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SeatInventoryTest {
    @Test
    fun `여러 좌석을 선택하여 예매할 수 있다`() {
        val seatInventory = SeatInventory.createDefaultSeatInventory()
        val seatNames = listOf("A1", "B2", "C3")

        val newSeatInventory = seatInventory.reserveSeats(seatNames)

        seatNames.forEach { name ->
            assertThat(newSeatInventory.findSeat(name).isReserved).isTrue
        }
    }

    @Test
    fun `이미 예약된 좌석은 선택될 수 없다`() {
        val seatInventory = SeatInventory.createDefaultSeatInventory()
        val seatNames = listOf("A1", "B2")

        val reservedInventory = seatInventory.reserveSeats(seatNames)

        assertThrows<IllegalArgumentException> {
            reservedInventory.reserveSeats(seatNames)
        }
    }

    @Test
    fun `존재하지 않는 좌석을 가져올 수 없다`() {
        val seatInventory = SeatInventory.createDefaultSeatInventory()

        assertThrows<IllegalArgumentException> {
            seatInventory.findSeat("Z9")
        }
    }

    @Test
    fun `여러 좌석의 총 금액을 계산할 수 있다`() {
        // A1: B_RANK(12,000) + C1: S_RANK(18,000) = 30,000
        val seatInventory = SeatInventory.createDefaultSeatInventory()
        val seatNames = listOf("A1", "C1")

        val totalPrice = seatInventory.calculatePrice(seatNames)

        assertThat(totalPrice.value).isEqualTo(30_000)
    }
}
