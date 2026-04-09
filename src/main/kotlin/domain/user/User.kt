package domain.user

import domain.Id

class User(val id: Id) {
    var point = Point(2000)

    init {
        require(id.value > 0) { "ID는 양수이어야 합니다." }
    }

    fun discountPoint(discount: Int) {
        point = point.discount(discount)
    }
}
