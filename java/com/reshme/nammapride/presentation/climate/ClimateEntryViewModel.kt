package com.reshme.nammapride.presentation.climate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.logic.LogicEngine
import com.reshme.nammapride.domain.model.Batch
import com.reshme.nammapride.domain.model.ClimateLog
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.util.DateTools
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClimateEntryState(val batch: Batch? = null, val temperature: Float = 26f, val humidity: Float = 78f, val instar: Int = 3, val savedAdvice: ClimateLog? = null, val error: String? = null)

class ClimateEntryViewModel(private val repository: ReshmeRepository) : ViewModel() {
    private val form = MutableStateFlow(ClimateEntryState())
    val state: StateFlow<ClimateEntryState> = combine(form, repository.observeActiveBatch()) { formState, batch -> formState.copy(batch = batch) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ClimateEntryState())

    fun onTemp(value: Float) = form.update { it.copy(temperature = value.coerceIn(10f, 45f), savedAdvice = null, error = null) }
    fun onHumidity(value: Float) = form.update { it.copy(humidity = value.coerceIn(40f, 95f), savedAdvice = null, error = null) }
    fun onInstar(value: Int) = form.update { it.copy(instar = value.coerceIn(1, 5), savedAdvice = null, error = null) }
    fun save() = viewModelScope.launch {
        val current = state.value
        val batch = current.batch ?: run { form.update { it.copy(error = "Start a batch before logging climate") }; return@launch }
        val advice = LogicEngine.evaluate(current.temperature, current.humidity, current.instar)
        val log = ClimateLog(0, batch.batchId, current.temperature, current.humidity, current.instar, advice.status, advice.title, advice.message, DateTools.millisNow())
        repository.addClimateLog(log)
        form.update { it.copy(savedAdvice = log, error = null) }
    }
}
