package model.seat

enum class SeatRank(
    val price: Price,
) {
    S_RANK(Price(18_000)),
    A_RANK(Price(15_000)),
    B_RANK(Price(12_000)),
}
