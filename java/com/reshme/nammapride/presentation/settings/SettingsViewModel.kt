package com.reshme.nammapride.presentation.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.notifications.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val Context.settingsStore by preferencesDataStore("reshme_settings")
data class SettingsState(val fahrenheit: Boolean = false, val notifications: Boolean = true)

class SettingsViewModel(private val context: Context, private val repository: ReshmeRepository) : ViewModel() {
    private val fahrenheitKey = booleanPreferencesKey("fahrenheit")
    private val notificationKey = booleanPreferencesKey("notifications")
    val state: StateFlow<SettingsState> = context.settingsStore.data.map { SettingsState(it[fahrenheitKey] ?: false, it[notificationKey] ?: true) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsState())
    fun setFahrenheit(enabled: Boolean) = viewModelScope.launch { context.settingsStore.edit { it[fahrenheitKey] = enabled } }
    fun setNotifications(enabled: Boolean) = viewModelScope.launch {
        context.settingsStore.edit { it[notificationKey] = enabled }
        if (!enabled) NotificationHelper.cancelAll(context)
    }
    fun clearData() = viewModelScope.launch { repository.clearAllData(); NotificationHelper.cancelAll(context) }
}
