package domain.reservation

import constants.ErrorMessages

class Cart(
    private val reservedScreen: List<ReservedScreen> = emptyList(),
) {
    val items: List<ReservedScreen> = reservedScreen.toList()

    fun add(screening: ReservedScreen): Cart {
        reservedScreen.forEach {
            require(!it.screen.overlaps(screening.screen)) {
                ErrorMessages.OVERLAP_MOVIE_TIME.message
            }
        }
        return Cart(reservedScreen + screening)
    }
}
