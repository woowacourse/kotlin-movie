package domain

class Screen(val seats: List<Seat>, val id: Int) {
    companion object {
        val maxRow = 5
        val maxColumn = 4
    }
}
