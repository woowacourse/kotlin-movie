package domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull

class Seat(row: Char, column: Int, grade: SeatGrade, isReserved: Boolean) {
    init {
        require(row.isUpperCase()) { "열은 한 글자 대문자 알파벳이여야 합니다." }
    }
}

enum class SeatGrade(val price: Int) {
    S(18_000),
    A(15_000),
    B(12_000),
}

class SeatTest {
    @Test
    fun `열이 한 글자 알파벳이면 좌석이 정상 생성된다`() {
        // given & when : Seat 객체를 생성할 때 한 글자 알파벳을 입력한다.
        val result = Seat('A', 1, SeatGrade.S, false)
        // then : 정상 생성된다.
        assertNotNull(result)
    }
}
