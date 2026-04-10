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

    fun addInfos(infos: ReservationInfo): Cart {
        return Cart(
            ReservationInfos(
                reservationInfos.infos + infos,
            ),
        )
    }

    fun showItems(): List<String> {
        return reservationInfos.getAllInfos()
    }

    fun getDiscountedItems(): Int {
        return reservationInfos.applyDiscount()
    }
}
