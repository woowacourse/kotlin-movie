package model.policy

import model.Money

sealed interface DiscountEffect

data class AmountDiscountEffect(val amount: Money) : DiscountEffect

data class RateDiscountEffect(val rate: Double) : DiscountEffect

data object NoDiscountEffect : DiscountEffect
