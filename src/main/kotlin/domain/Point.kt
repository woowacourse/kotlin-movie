package domain

data class Point(val amount: Int) {

    init {
        require(amount >= 0) { "포인트는 음수일 수 없습니다." }
    }
}
