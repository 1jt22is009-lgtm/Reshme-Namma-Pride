package com.reshme.nammapride

import android.app.Application
import com.reshme.nammapride.data.local.ReshmeDatabase
import com.reshme.nammapride.data.repository.ReshmeRepositoryImpl
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.notifications.NotificationHelper

class ReshmeApp : Application() {
    lateinit var repository: ReshmeRepository
        private set

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createChannel(this)
        val database = ReshmeDatabase.getDatabase(this)
        repository = ReshmeRepositoryImpl(database.batchDao(), database.climateLogDao())
    }
}
