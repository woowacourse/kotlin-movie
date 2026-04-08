package domain

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MovieTest {

    @Test
    fun `영화 제목이 공백일 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) { Title(title = "") }
    }

    @Test
    fun `영화의 상영 길이가 0이하일 경우 예외를 던진다`() {
        assertThrows(IllegalArgumentException::class.java) { RunningTime(duration = 0) }
    }

    @Test
    fun `종료일이 시작일보다 앞 설 경우 예외를 던진다`() {

        val startDate = LocalDate.of(2026, 4, 8)
        val endDate = LocalDate.of(2026, 4, 7)
        assertThrows(IllegalArgumentException::class.java) {
            ScreeningPeriod(
                startDate = startDate,
                endDate = endDate
            )
        }
    }

    @Test
    fun `영화는 제목, 상영 길이, 상영 기간을 가진다`() {
        Movie(
            title = Title(title = "안녕하세요 커피입니다"),
            runningTime = RunningTime(duration = 178),
            screeningPeriod = ScreeningPeriod(
                startDate = LocalDate.of(2026, 4, 7),
                endDate = LocalDate.of(2026, 4, 8)
            ),
        )
    }
}
