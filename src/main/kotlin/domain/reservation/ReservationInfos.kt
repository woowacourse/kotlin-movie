package domain.reservation

class ReservationInfos(val infos: List<ReservationInfo>) {
    fun getAllInfos(): List<String> {
        return infos.map { item ->
            item.toString()
        }
    }
}
