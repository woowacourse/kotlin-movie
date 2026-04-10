package domain.user

import domain.Id

data class User(val id: Id, val point: Point = Point(2000)) {
    init {
        require(id.value > 0) { "ID는 양수이어야 합니다." }
    }

    fun discountPoint(discount: Point): User {
        return copy(point = point.discount(discount.point))
    }
}
