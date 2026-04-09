package domain

class ReservationInfo(val showing: Showing, val seat: Seat) {
    companion object {
        fun create(
            showing: Showing,
            seat: Seat,
        ): ReservationInfo {
            return ReservationInfo(
                showing,
                seat,
            )
        }
    }
}
