package domain.movie

import domain.screening.Screenings
import java.util.UUID

data class Movie(
    val id: UUID,
    val screenings: Screenings,
)
