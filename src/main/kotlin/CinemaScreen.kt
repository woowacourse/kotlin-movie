import java.time.LocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class CinemaScreen(
    val id: Uuid,
    val servicePeriod: DateTimeRange,
) {
    fun isContainServicePeriod(time: LocalDateTime): Boolean = servicePeriod.contains(time)

    override fun equals(other: Any?): Boolean {
        if (other is CinemaScreen) {
            return this.id == other.id
        }
        return false
    }

    override fun hashCode(): Int = id.hashCode()
}
