package domain

class Cart(val reservationInfos: List<ReservationInfo>) {
    fun addInfo(info: ReservationInfo): Cart {
        return Cart(
            reservationInfos.plus(info),
        )
    }

    fun getTotalPrice(): Int {
        var price = 0
        reservationInfos.forEach {
            price += it.seat.grade.price
        }
        return price
    }
}
