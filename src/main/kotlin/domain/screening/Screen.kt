package domain.screening

import domain.seat.Seats

data class Screen(
    val screenName: String,
    val seats: Seats
)
