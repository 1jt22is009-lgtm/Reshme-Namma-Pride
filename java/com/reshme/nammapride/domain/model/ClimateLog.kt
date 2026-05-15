package com.reshme.nammapride.domain.model

data class ClimateLog(
    val id: Long,
    val batchId: String,
    val temperatureC: Float,
    val humidity: Float,
    val instar: Int,
    val status: ClimateStatus,
    val adviceTitle: String,
    val adviceMessage: String,
    val createdAtMillis: Long
)

enum class ClimateStatus { Optimal, Warning, Danger }
