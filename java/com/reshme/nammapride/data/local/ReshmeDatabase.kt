package com.reshme.nammapride.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [BatchEntity::class, ClimateLogEntity::class], version = 4, exportSchema = true)
abstract class ReshmeDatabase : RoomDatabase() {
    abstract fun batchDao(): BatchDao
    abstract fun climateLogDao(): ClimateLogDao

    companion object {
        private const val DB_NAME = "reshme_namma_pride.db"
        
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE batches ADD COLUMN expectedHarvestEpochDay INTEGER NOT NULL DEFAULT 0")
            }
        }
        
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS silk_listings (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        farmerName TEXT NOT NULL,
                        phoneNumber TEXT NOT NULL,
                        location TEXT NOT NULL,
                        silkType TEXT NOT NULL,
                        grade TEXT NOT NULL,
                        freshnessIndex INTEGER NOT NULL,
                        gsm REAL NOT NULL,
                        denier REAL NOT NULL,
                        quantityKg REAL NOT NULL,
                        pricePerKg REAL NOT NULL,
                        notes TEXT NOT NULL,
                        createdAtMillis INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS silk_listings")
            }
        }

        @Volatile private var INSTANCE: ReshmeDatabase? = null
        fun getDatabase(context: Context): ReshmeDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, ReshmeDatabase::class.java, DB_NAME)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .fallbackToDestructiveMigration() // Add this as a fallback
                .build().also { INSTANCE = it }
        }
    }
}
