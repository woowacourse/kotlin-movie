package domain.reservation

class Cart(val reservationInfos: List<ReservationInfo>) {
    fun addInfo(info: ReservationInfo): Cart {
        return Cart(
            reservationInfos.plus(info),
        )
    }
}
