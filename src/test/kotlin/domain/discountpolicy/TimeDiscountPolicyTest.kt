package domain.discountpolicy

import domain.timetable.items.ScreenTime
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class TimeDiscountPolicyTest {
    @Test
    fun `예약 시간이 오전 11이전 또는 오후 8시 이후라면 2,000 할인한 금액을 반환한다`() {
        val time =
            ScreenTime(
                startTime = LocalTime.of(10, 0),
                endTime = LocalTime.of(12, 0),
                screeningDate = LocalDate.of(2026, 4, 10),
            )
    }

    @Test
    fun `시간에 해당하지 않는 다면 받은 금액을 반환한다`() {
    }

    @Test
    fun `예약 날짜가 10, 20, 30일 중 하나라면 10% 할인한 금액을 반환한다`() {
    }

    @Test
    fun `예약 날짜에 해당하지 않는 다면 받은 금액을 반환한다`() {
    }
}
