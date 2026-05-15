package com.reshme.nammapride.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk

@Composable
fun SettingsScreen(repository: ReshmeRepository) {
    val context = LocalContext.current.applicationContext
    val vm: SettingsViewModel = viewModel(factory = RepositoryViewModelFactory { SettingsViewModel(context, repository) })
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Settings", "Local-only controls for units, alerts, and data")
        GlassCard(Modifier.fillMaxWidth().padding(18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                SettingRow("Temperature Unit", if (state.fahrenheit) "Fahrenheit" else "Celsius") { Switch(state.fahrenheit, vm::setFahrenheit) }
                SettingRow("Harvest Notifications", if (state.notifications) "Enabled" else "Paused") { Switch(state.notifications, vm::setNotifications) }
                OutlinedButton(onClick = vm::clearData, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error), modifier = Modifier.fillMaxWidth()) { Text("Clear Local Data") }
                Text("All records stay on this device. No login, Firebase, internet, or external API is used.", color = ElectricSilk.copy(0.65f))
            }
        }
    }
}

@Composable
private fun SettingRow(title: String, value: String, control: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Text(title, color = ElectricSilk, style = MaterialTheme.typography.titleLarge)
            Text(value, color = EcoNeon)
        }
        control()
    }
}
