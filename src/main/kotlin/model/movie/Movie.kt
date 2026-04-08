package model.movie

import model.movie.RunningTime
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class Movie(
    val id: String,
    val runningTime: RunningTime,
) {
    override fun equals(other: Any?): Boolean {
        if (other is Movie) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = id.hashCode()
}
