package domain

import java.time.LocalDate

data class ScreeningPeriod(val startDate: LocalDate, val endDate: LocalDate) {
    init {
        require(endDate.isBefore(startDate).not()) {
            "종료일이 시작일보다 앞설 수 없습니다."
        }
    }
}
