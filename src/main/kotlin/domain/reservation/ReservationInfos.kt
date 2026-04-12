package domain.reservation

import domain.cinema.Showing
import domain.discount.MovieDayDiscount
import domain.discount.TimeDiscount

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

    fun applyDiscount(): Int {
        return infos.sumOf {
            var initPrice = it.seats.getAllPrice()

            val movieDayDiscount = MovieDayDiscount()
            if (movieDayDiscount.isApplicable(it.showing.startTime)) {
                initPrice = movieDayDiscount.discountApply(initPrice)
            }

            val timeDiscount = TimeDiscount()
            if (timeDiscount.isApplicable(it.showing.startTime)) {
                initPrice = timeDiscount.discountApply(initPrice)
            }
            initPrice.price
        }
    }
}
