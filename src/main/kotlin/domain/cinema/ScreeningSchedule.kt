package domain.cinema

import domain.purchase.policy.screening.MovieDayDiscountPolicy
import domain.purchase.policy.screening.ScreeningDiscountPolicy
import domain.purchase.policy.screening.ShowTimeDiscountPolicy
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

class ScreeningSchedule(
    val startTime: LocalDateTime,
    val screen: Screen,
    val movie: Movie,
) {
    val endTime =
        startTime
            .toInstant(TimeZone.currentSystemDefault())
            .plus(movie.runningTime.minutes)
            .toLocalDateTime(TimeZone.currentSystemDefault())

    fun calculatePrice(basePrice: Int): Int =
        screeningDiscountPolicies.fold(basePrice) { discountedPrice, policy ->
            policy.apply(discountedPrice, startTime)
        }

    companion object {
        private val screeningDiscountPolicies: List<ScreeningDiscountPolicy> =
            listOf(
                MovieDayDiscountPolicy(),
                ShowTimeDiscountPolicy(),
            )
    }
}
