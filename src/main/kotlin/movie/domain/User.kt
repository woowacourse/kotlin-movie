package movie.domain

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class User(
    val userId: Uuid = Uuid.random(),
    private var point: Point = Point(0)
) {
    fun useUserPoint(usePoint: Point) {
        point = point.use(usePoint)
    }
}
