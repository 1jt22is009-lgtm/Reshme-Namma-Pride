package com.reshme.nammapride.presentation.harvest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.util.DateTools
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HarvestState(val batch: Batch? = null, val daysLeft: Long = 0, val progress: Float = 0f)
class HarvestViewModel(repository: ReshmeRepository) : ViewModel() {
    val state: StateFlow<HarvestState> = repository.observeActiveBatch().map { batch ->
        val today = DateTools.todayEpochDay()
        val left = batch?.let { DateTools.daysBetween(today, it.expectedHarvestEpochDay).coerceAtLeast(0) } ?: 0
        val total = batch?.let { DateTools.daysBetween(it.startDateEpochDay, it.expectedHarvestEpochDay).coerceAtLeast(1) } ?: 1
        val done = batch?.let { DateTools.daysBetween(it.startDateEpochDay, today).coerceAtLeast(0) } ?: 0
        HarvestState(batch, left, (done.toFloat() / total).coerceIn(0f, 1f))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HarvestState())
}
