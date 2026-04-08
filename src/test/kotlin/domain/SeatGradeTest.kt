package domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SeatGradeTest {
    @ParameterizedTest(name = "등급: {0}, 가격: {1}")
    @CsvSource(
        "S,18_000",
        "A, 15_000",
        "B, 12_000",
    )
    fun `등급에 대한 가격을 가진다`(
        given: SeatGrade,
        expected: Int,
    ) {
        assertThat(given.price).isEqualTo(expected)
    }

    @ParameterizedTest(name = "행: {0}, 열: {1}")
    @CsvSource(
        "A, 1, B",
        "B, 1, B",
        "C, 1, S",
        "D, 1, S",
        "E, 1, A",
    )
    fun `행과 열에 대한 등급을 반환한다`(
        givenR: Row,
        givenC: Column,
        expected: SeatGrade,
    ) {
        val result = SeatGrade.of(row = givenR, column = givenC)
        assertThat(result).isEqualTo(expected)
    }
}
