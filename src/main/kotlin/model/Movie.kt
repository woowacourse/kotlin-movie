package model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Movie(
    val id: Uuid,
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
