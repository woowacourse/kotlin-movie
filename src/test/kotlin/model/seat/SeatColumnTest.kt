package model.seat

import model.seat.SeatColumn
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SeatColumnTest {
    @ParameterizedTest
    @ValueSource(ints = [0, 6, -1, 33])
    fun `좌석의 열 번호가 1 미만 4 초과일 경우 예외를 던진다`(column: Int) {
        assertThatThrownBy {
            SeatColumn(column = column)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 2, 3, 4])
    fun `좌석의 열 번호가 1 이상 4 이하일 경우 예외를 던지지 않는다`(column: Int) {
        assertThatCode {
            SeatColumn(column = column)
        }.doesNotThrowAnyException()
    }
}
