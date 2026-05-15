package com.reshme.nammapride.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.logic.LogicEngine
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.model.ClimateLog
import com.reshme.nammapride.domain.repository.ReshmeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DashboardState(val batch: Batch? = null, val latest: ClimateLog? = null, val logs: List<ClimateLog> = emptyList(), val successRate: Int = 0)

class DashboardViewModel(repository: ReshmeRepository) : ViewModel() {
    val state: StateFlow<DashboardState> = combine(repository.observeActiveBatch(), repository.observeAllLogs()) { batch, logs ->
        val batchLogs = batch?.let { active -> logs.filter { it.batchId == active.batchId } } ?: emptyList()
        DashboardState(batch, batchLogs.maxByOrNull { it.createdAtMillis }, batchLogs, LogicEngine.successRate(batchLogs))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DashboardState())
}
