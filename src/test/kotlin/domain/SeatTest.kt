package domain

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SeatTest {
    @Test
    fun `행과 열을 가진다`() {
        Seat(row = "A", column = 1)
    }

    @Test
    fun `행이 A~J 범위가 아닐 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Seat(row = "K", column = 1)
        }
    }

    @Test
    fun `행이 알파벳이 아닌 문자가 포함될 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Seat(row = "Cㅓ비", column = 1)
        }
    }

    @Test
    fun `열이 1~12 범위가 아닐 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            Seat(row = "J", column = 13)
        }
    }
}
