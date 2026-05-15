package com.reshme.nammapride.data.repository

import com.reshme.nammapride.data.local.BatchDao
import com.reshme.nammapride.data.local.BatchEntity
import com.reshme.nammapride.data.local.ClimateLogDao
import com.reshme.nammapride.data.local.ClimateLogEntity
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.model.ClimateLog
import com.reshme.nammapride.domain.model.ClimateStatus
import com.reshme.nammapride.domain.model.SilkPricePoint
import com.reshme.nammapride.domain.model.SilkVariety
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.util.DateTools
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ReshmeRepositoryImpl(
    private val batchDao: BatchDao, 
    private val logDao: ClimateLogDao
) : ReshmeRepository {
    override fun observeActiveBatch(): Flow<Batch?> = batchDao.observeActiveBatch().map { it?.toDomain() }
    override fun observeBatches(): Flow<List<Batch>> = batchDao.observeBatches().map { rows -> rows.map { it.toDomain() } }
    override fun observeAllLogs(): Flow<List<ClimateLog>> = logDao.observeAllLogs().map { rows -> rows.map { it.toDomain() } }
    override fun observeLogsForBatch(batchId: String): Flow<List<ClimateLog>> = logDao.observeLogsForBatch(batchId).map { rows -> rows.map { it.toDomain() } }

    override fun observeSilkPrices(): Flow<List<SilkVariety>> = flowOf(
        listOf(
            SilkVariety(
                name = "Bivoltine White",
                currentPrice = 4500f,
                dailyChange = 1.2f,
                monthlyChange = -3.5f,
                yearlyChange = 12.8f,
                history = generateMockHistory(4500f)
            ),
            SilkVariety(
                name = "Cross Breed",
                currentPrice = 3200f,
                dailyChange = -0.5f,
                monthlyChange = 2.1f,
                yearlyChange = 8.4f,
                history = generateMockHistory(3200f)
            ),
            SilkVariety(
                name = "Mulberry Raw",
                currentPrice = 5100f,
                dailyChange = 0.8f,
                monthlyChange = 4.2f,
                yearlyChange = 15.1f,
                history = generateMockHistory(5100f)
            )
        )
    )

    private fun generateMockHistory(basePrice: Float): List<SilkPricePoint> {
        val now = DateTools.millisNow()
        val dayMillis = 86400000L
        return (0..30).map { i ->
            SilkPricePoint(
                price = basePrice + (Math.random().toFloat() * 400 - 200),
                timestamp = now - (30 - i) * dayMillis
            )
        }
    }

    override suspend fun startBatch(batch: Batch) {
        batchDao.deactivateAll()
        batchDao.upsert(batch.toEntity())
    }

    override suspend fun addClimateLog(log: ClimateLog) { logDao.insert(log.toEntity()) }
    
    override suspend fun clearAllData() { 
        logDao.clearAll()
        batchDao.clearAll() 
    }
}

private fun BatchEntity.toDomain() = Batch(batchId, breedName, startDateEpochDay, createdAtMillis, isActive, expectedHarvestEpochDay)
private fun Batch.toEntity() = BatchEntity(batchId, breedName, startDateEpochDay, createdAtMillis, isActive, expectedHarvestEpochDay)
private fun ClimateLogEntity.toDomain() = ClimateLog(id, batchId, temperatureC, humidity, instar, ClimateStatus.valueOf(status), adviceTitle, adviceMessage, createdAtMillis)
private fun ClimateLog.toEntity() = ClimateLogEntity(id, batchId, temperatureC, humidity, instar, status.name, adviceTitle, adviceMessage, createdAtMillis)
