import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class Movie(
    val id: Uuid,
    val runningTime: RunningTime,
)
