package model

import model.seat.Seats

data class Screen(
    val screenName: String,
    val seats: Seats,
)
