package com.reshme.nammapride.domain.model

data class Batch(
    val batchId: String,
    val breedName: String,
    val startDateEpochDay: Long,
    val createdAtMillis: Long,
    val isActive: Boolean,
    val expectedHarvestEpochDay: Long
)
