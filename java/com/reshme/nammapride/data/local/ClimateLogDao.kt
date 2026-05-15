package com.reshme.nammapride.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClimateLogDao {
    @Insert
    suspend fun insert(log: ClimateLogEntity): Long

    @Query("SELECT * FROM climate_logs ORDER BY createdAtMillis DESC")
    fun observeAllLogs(): Flow<List<ClimateLogEntity>>

    @Query("SELECT * FROM climate_logs WHERE batchId = :batchId ORDER BY createdAtMillis DESC")
    fun observeLogsForBatch(batchId: String): Flow<List<ClimateLogEntity>>

    @Query("SELECT * FROM climate_logs WHERE batchId = :batchId ORDER BY createdAtMillis DESC LIMIT 1")
    fun observeLatestForBatch(batchId: String): Flow<ClimateLogEntity?>

    @Query("DELETE FROM climate_logs")
    suspend fun clearAll()
}
