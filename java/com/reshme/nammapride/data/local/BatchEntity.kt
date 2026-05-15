package com.reshme.nammapride.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batches")
data class BatchEntity(
    @PrimaryKey val batchId: String,
    val breedName: String,
    val startDateEpochDay: Long,
    val createdAtMillis: Long,
    val isActive: Boolean = true,
    val expectedHarvestEpochDay: Long
)
