package domain.user

class User(val id: Int) {
    var point = Point(2000)

    init {
        require(id > 0) { "ID는 양수이어야 합니다." }
    }

    fun discountPoint(discount: Int) {
        point = point.discount(discount)
    }
}
