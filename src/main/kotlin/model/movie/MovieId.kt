package model.movie

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JvmInline
@OptIn(ExperimentalUuidApi::class)
value class MovieId(
    val id: Uuid
)