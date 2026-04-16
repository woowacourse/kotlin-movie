package movie.model

import movie.model.seat.Seats

data class Screen(
    val screenName: String,
    val seats: Seats,
)
