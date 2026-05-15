package com.reshme.nammapride.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SilkListingDao {
    @Insert
    suspend fun insert(listing: SilkListingEntity): Long

    @Query("SELECT * FROM silk_listings ORDER BY createdAtMillis DESC")
    fun observeListings(): Flow<List<SilkListingEntity>>

    @Query("DELETE FROM silk_listings")
    suspend fun clearAll()
}
