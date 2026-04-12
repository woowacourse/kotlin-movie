package domain.reservation

import domain.cinema.Showing

class ReservationInfos(val infos: List<ReservationInfo>) {
    fun getAllInfos(): List<ReservationInfo> {
        return infos.toList()
    }

    fun checkReservationHistory(showing: Showing) {
        val history = infos.filter {
            showing.startTime >= it.showing.startTime && showing.startTime <= it.showing.endTime
        }

        require(history.isEmpty()) { "선택하신 상영 시간이 겹칩니다. 다른 시간을 선택해 주세요." }
    }

    fun applyAllDiscount(): Int {
        return infos.sumOf {
            it.applyDiscount()
        }
    }
}
