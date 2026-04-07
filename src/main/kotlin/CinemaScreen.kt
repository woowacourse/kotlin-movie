import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CinemaScreen(
    val id: Uuid,
    val dateTimeRange: DateTimeRange,
) {
    override fun equals(other: Any?): Boolean {
        if (other is CinemaScreen) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = id.hashCode()
}
