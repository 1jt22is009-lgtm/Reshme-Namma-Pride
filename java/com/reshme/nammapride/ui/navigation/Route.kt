package com.reshme.nammapride.ui.navigation

sealed class Route(val value: String) {
    data object Splash : Route("splash")
    data object Dashboard : Route("dashboard")
    data object NewBatch : Route("new_batch")
    data object ClimateEntry : Route("climate_entry")
    data object Advice : Route("advice")
    data object History : Route("history")
    data object Marketplace : Route("marketplace")
    data object Harvest : Route("harvest")
    data object Settings : Route("settings")
}
