package domain

import java.time.LocalTime

data class ScreeningRoom(
    val name: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val seats: List<Seat>
) {

    init {
        require(name.isNotBlank()) { "이름은 공백일 수 없습니다." }
        require(endTime.isAfter(startTime)){"종료시간이 시작시간보다 앞설 수 없습니다."}
        require(seats.isNotEmpty()){"좌석 목록은 비어 있을 수 없습니다."}
    }
}
