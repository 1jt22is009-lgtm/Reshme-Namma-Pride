package com.reshme.nammapride.presentation.batch

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.logic.LogicEngine
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.notifications.NotificationHelper
import com.reshme.nammapride.util.DateTools
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NewBatchState(val breed: String = "", val batchId: String = "", val startDate: String = LocalDate.now().toString(), val error: String? = null, val saved: Boolean = false)

class NewBatchViewModel(private val repository: ReshmeRepository) : ViewModel() {
    private val _state = MutableStateFlow(NewBatchState())
    val state: StateFlow<NewBatchState> = _state
    fun onBreed(value: String) = _state.update { it.copy(breed = value, saved = false, error = null) }
    fun onBatchId(value: String) = _state.update { it.copy(batchId = value, saved = false, error = null) }
    fun onStartDate(value: String) = _state.update { it.copy(startDate = value, saved = false, error = null) }
    fun save(context: Context) = viewModelScope.launch {
        val current = _state.value
        val date = runCatching { LocalDate.parse(current.startDate) }.getOrNull()
        if (current.breed.isBlank() || current.batchId.isBlank() || date == null) {
            _state.update { it.copy(error = "Enter breed, batch ID, and start date as YYYY-MM-DD") }
            return@launch
        }
        val harvest = date.plusDays(LogicEngine.harvestDaysForBreed(current.breed).toLong()).toEpochDay()
        val batch = Batch(current.batchId.trim(), current.breed.trim(), date.toEpochDay(), DateTools.millisNow(), true, harvest)
        repository.startBatch(batch)
        NotificationHelper.scheduleHarvestAlert(context, batch.batchId, batch.breedName, harvest)
        _state.update { it.copy(saved = true, error = null) }
    }
}
