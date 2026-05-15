package com.reshme.nammapride.domain.model

data class SilkPricePoint(
    val price: Float,
    val timestamp: Long
)

data class SilkVariety(
    val name: String,
    val currentPrice: Float,
    val dailyChange: Float, // percentage
    val monthlyChange: Float,
    val yearlyChange: Float,
    val history: List<SilkPricePoint>
)
