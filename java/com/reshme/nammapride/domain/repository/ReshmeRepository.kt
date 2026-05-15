package com.reshme.nammapride.domain.repository

import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.model.ClimateLog
import com.reshme.nammapride.domain.model.SilkVariety
import kotlinx.coroutines.flow.Flow

interface ReshmeRepository {
    fun observeActiveBatch(): Flow<Batch?>
    fun observeBatches(): Flow<List<Batch>>
    fun observeAllLogs(): Flow<List<ClimateLog>>
    fun observeLogsForBatch(batchId: String): Flow<List<ClimateLog>>
    fun observeSilkPrices(): Flow<List<SilkVariety>>
    suspend fun startBatch(batch: Batch)
    suspend fun addClimateLog(log: ClimateLog)
    suspend fun clearAllData()
}
