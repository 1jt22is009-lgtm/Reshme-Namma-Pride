package com.reshme.nammapride.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "silk_listings")
data class SilkListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
