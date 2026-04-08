package movie.domain.movie

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Movie(
    val id: Uuid = Uuid.random(),
    val title: MovieTitle,
)
