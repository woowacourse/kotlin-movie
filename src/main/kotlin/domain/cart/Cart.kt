package domain.cart

import domain.reservation.ReservationInfo
import domain.reservation.ReservationInfos

class Cart(val reservationInfos: ReservationInfos) {
    fun addInfo(info: ReservationInfo): Cart {
        return Cart(
            ReservationInfos(
                reservationInfos.infos + info,
            ),
        )
    }
    fun showItems(): List<ReservationInfo> {
        return reservationInfos.getAllInfos()
    }

    fun getDiscountedItems(): Int {
        return reservationInfos.applyAllDiscount()
    }
}
