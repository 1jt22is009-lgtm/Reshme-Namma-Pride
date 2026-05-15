package com.reshme.nammapride.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "climate_logs",
    foreignKeys = [ForeignKey(entity = BatchEntity::class, parentColumns = ["batchId"], childColumns = ["batchId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("batchId"), Index("createdAtMillis")]
)
data class ClimateLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val batchId: String,
    val temperatureC: Float,
    val humidity: Float,
    val instar: Int,
    val status: String,
    val adviceTitle: String,
    val adviceMessage: String,
    val createdAtMillis: Long
)
