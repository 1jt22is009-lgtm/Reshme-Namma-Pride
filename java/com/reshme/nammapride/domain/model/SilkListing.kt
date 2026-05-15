package com.reshme.nammapride.domain.model

data class SilkListing(
    val id: Long,
    val farmerName: String,
    val phoneNumber: String,
    val location: String,
    val silkType: String,
    val grade: String,
    val freshnessIndex: Int,
    val gsm: Float,
    val denier: Float,
    val quantityKg: Float,
    val pricePerKg: Float,
    val notes: String,
    val createdAtMillis: Long
)
