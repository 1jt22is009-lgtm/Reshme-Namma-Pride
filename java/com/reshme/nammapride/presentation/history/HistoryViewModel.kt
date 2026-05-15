package com.reshme.nammapride.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.model.ClimateLog
import com.reshme.nammapride.domain.repository.ReshmeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HistoryState(val batches: List<Batch> = emptyList(), val logs: List<ClimateLog> = emptyList())
class HistoryViewModel(repository: ReshmeRepository) : ViewModel() {
    val state: StateFlow<HistoryState> = combine(repository.observeBatches(), repository.observeAllLogs()) { batches, logs -> HistoryState(batches, logs) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HistoryState())
}
