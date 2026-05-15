package com.reshme.nammapride.domain.logic

import com.reshme.nammapride.domain.model.ClimateStatus

data class InstarRule(val instar: Int, val tempMin: Float, val tempMax: Float, val humidityMin: Float, val humidityMax: Float)
data class AdviceResult(val status: ClimateStatus, val title: String, val message: String)

object LogicEngine {
    val rules = listOf(
        InstarRule(1, 26f, 28f, 85f, 90f),
        InstarRule(2, 26f, 28f, 80f, 85f),
        InstarRule(3, 25f, 27f, 75f, 80f),
        InstarRule(4, 24f, 26f, 70f, 75f),
        InstarRule(5, 23f, 25f, 65f, 70f)
    )

    fun evaluate(temperatureC: Float, humidity: Float, instar: Int): AdviceResult {
        val rule = rules.first { it.instar == instar.coerceIn(1, 5) }
        val tempGap = distanceOutside(temperatureC, rule.tempMin, rule.tempMax)
        val humidityGap = distanceOutside(humidity, rule.humidityMin, rule.humidityMax)
        val status = when {
            tempGap == 0f && humidityGap == 0f -> ClimateStatus.Optimal
            tempGap <= 2f && humidityGap <= 8f -> ClimateStatus.Warning
            else -> ClimateStatus.Danger
        }
        val actions = buildList {
            if (temperatureC > rule.tempMax) add("Open windows, thin out crowded trays, and improve cross-ventilation immediately.")
            if (temperatureC < rule.tempMin) add("Close vents during cool hours and add safe room warming away from rearing trays.")
            if (humidity > rule.humidityMax) add("Reduce wet floor sprinkling, increase airflow, and remove stale mulberry leaves.")
            if (humidity < rule.humidityMin) add("Spread wet gunny bags, sprinkle clean water on the floor, and cover trays lightly.")
            if (isEmpty()) add("Conditions match Instar $instar. Continue clean feeding, spacing, and steady ventilation.")
        }
        val title = when (status) {
            ClimateStatus.Optimal -> "Silkworm zone is stable"
            ClimateStatus.Warning -> "Adjustment needed for Instar $instar"
            ClimateStatus.Danger -> "Critical climate intervention"
        }
        return AdviceResult(status, title, actions.joinToString(" "))
    }

    fun successRate(logs: List<com.reshme.nammapride.domain.model.ClimateLog>): Int {
        if (logs.isEmpty()) return 0
        return ((logs.count { it.status == ClimateStatus.Optimal }.toFloat() / logs.size) * 100).toInt()
    }

    fun harvestDaysForBreed(breedName: String): Int = when {
        breedName.contains("bivoltine", ignoreCase = true) -> 27
        breedName.contains("hybrid", ignoreCase = true) -> 26
        breedName.contains("cross", ignoreCase = true) -> 25
        else -> 26
    }

    private fun distanceOutside(value: Float, min: Float, max: Float): Float = when {
        value < min -> min - value
        value > max -> value - max
        else -> 0f
    }
}
