package domain
class User(val id: Long) {
    var point: Long = 1000

    init {
        require(id > 0) { "ID는 양수이어야 합니다." }
    }

    fun discountPoint(discount: Long) {
        require(point - discount > 0) { "차감액은 전체 포인트보다 작아야 합니다." }
        point -= discount
    }
}
