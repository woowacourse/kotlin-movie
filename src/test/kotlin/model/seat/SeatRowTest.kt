package model.seat

import model.seat.SeatRow
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class SeatRowTest {
    @ParameterizedTest
    @ValueSource(strings = ["F", "AB", "-1", " ", "0", "a", "f", "B "])
    fun `좌석의 행 문자가 F 이상이거나, 소문자로 입력되거나, 2글자 이상일 경우 예외를 던진다`(row: String) {
        assertThatThrownBy {
            SeatRow(row = row)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @ValueSource(strings = ["A", "B", "C", "D", "E"])
    fun `좌석의 행 문자가 A ~ E 범위이면 예외를 던지지 않는다`(row: String) {
        assertThatCode {
            SeatRow(row = row)
        }.doesNotThrowAnyException()
    }
}
