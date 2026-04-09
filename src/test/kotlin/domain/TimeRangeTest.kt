package domain

import domain.model.TimeRange
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalTime

class TimeRangeTest {
    @Test
    fun `종료시간이 시작시간보다 앞서면 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) {
            TimeRange(LocalTime.of(10, 0), LocalTime.of(9, 0))
        }
    }

    @Test
    fun `시간 범위가 겹치지 않으면 false를 반환한다`() {
        val givenA = TimeRange(LocalTime.of(10, 0), LocalTime.of(11, 0))
        val givenB = TimeRange(LocalTime.of(11, 0), LocalTime.of(12, 0))

        val result = givenA.isOverlapping(givenB)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `시간 범위가 겹치면 true를 반환한다`() {
        val givenA = TimeRange(LocalTime.of(10, 0), LocalTime.of(11, 1))
        val givenB = TimeRange(LocalTime.of(11, 0), LocalTime.of(12, 0))

        val result = givenA.isOverlapping(givenB)

        assertThat(result).isEqualTo(true)
    }
}
