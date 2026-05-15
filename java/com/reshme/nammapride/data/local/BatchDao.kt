package com.reshme.nammapride.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BatchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(batch: BatchEntity)

    @Query("UPDATE batches SET isActive = 0 WHERE isActive = 1")
    suspend fun deactivateAll()

    @Query("SELECT * FROM batches WHERE isActive = 1 ORDER BY createdAtMillis DESC LIMIT 1")
    fun observeActiveBatch(): Flow<BatchEntity?>

    @Query("SELECT * FROM batches ORDER BY createdAtMillis DESC")
    fun observeBatches(): Flow<List<BatchEntity>>

    @Query("DELETE FROM batches")
    suspend fun clearAll()
}
