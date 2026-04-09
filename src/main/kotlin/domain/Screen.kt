package domain

class Screen(val seats: List<Seat>, val id: Int) {
    companion object {
        const val MAX_ROW = 5
        const val MAX_COLUMN = 4
    }
}
