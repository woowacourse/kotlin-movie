package domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows

class SeatTest {
    @Test
    fun `열이 한 글자 알파벳이면 좌석이 정상 생성된다`() {
        // given & when : Seat 객체를 생성할 때 한 글자 알파벳을 입력한다.
        val result = Seat('A', 1, SeatGrade.S, false)
        // then : 정상 생성된다.
        assertNotNull(result)
    }

    @Test
    fun `열이 한 글자 대문자 알파벳이 아니면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 한 글자 소문자 알파벳을 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat('a', 1, SeatGrade.S, false)
        }

        // then : 예외가 발생한다.
        assertEquals("열은 한 글자 대문자 알파벳이여야 합니다.", exception.message)
    }

    @Test
    fun `행이 양수이면 좌석이 정상 생성된다`() {
        // given & when : Seat 객체를 생성할 때 행에 양수를 입력한다.
        val result = Seat('A', 1, SeatGrade.S, false)
        // then : 정상 생성된다.
        assertNotNull(result)
    }

    @Test
    fun `행이 0이면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 행에 0을 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat('A', 0, SeatGrade.S, false)
        }

        // then : 예외가 발생한다.
        assertEquals("행은 양수이여야 합니다.", exception.message)
    }

    @Test
    fun `행이 음수이면 예외가 발생한다`() {
        // given & when : Seat 객체를 생성할 때 행에 음수를 입력한다.
        val exception = assertThrows<IllegalArgumentException> {
            Seat('A', -1, SeatGrade.S, false)
        }

        // then : 예외가 발생한다.
        assertEquals("행은 양수이여야 합니다.", exception.message)
    }
}
