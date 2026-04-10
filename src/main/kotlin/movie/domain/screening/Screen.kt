package movie.domain.screening

import movie.domain.seat.Seats

class Screen(
    val id: ScreenId,
    val seats: Seats = Seats.createDefault(),
)
