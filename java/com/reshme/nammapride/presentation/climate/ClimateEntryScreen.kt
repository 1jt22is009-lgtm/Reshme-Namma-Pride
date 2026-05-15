package com.reshme.nammapride.presentation.climate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.reshme.nammapride.domain.logic.LogicEngine
import com.reshme.nammapride.domain.model.ClimateStatus
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.ClimateDial
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.navigation.Route
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk

@Composable
fun ClimateEntryScreen(repository: ReshmeRepository, navController: NavHostController) {
    val vm: ClimateEntryViewModel = viewModel(factory = RepositoryViewModelFactory { ClimateEntryViewModel(repository) })
    val state by vm.state.collectAsState()
    val preview = LogicEngine.evaluate(state.temperature, state.humidity, state.instar)
    LaunchedEffect(state.savedAdvice) { if (state.savedAdvice != null) navController.navigate(Route.Advice.value) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp)) {
        ScreenTitle("Climate Log", "Manual temperature, humidity, and Instar capture")
        ClimateDial(state.temperature, state.humidity, preview.status, Modifier.fillMaxWidth().height(280.dp).padding(horizontal = 34.dp).padding(top = 4.dp))
        GlassCard(Modifier.fillMaxWidth().padding(18.dp), accent = when (preview.status) { ClimateStatus.Optimal -> EcoNeon; ClimateStatus.Warning -> MaterialTheme.colorScheme.secondary; ClimateStatus.Danger -> MaterialTheme.colorScheme.error }) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(state.batch?.let { "Active: ${it.batchId}" } ?: "No active batch", color = ElectricSilk, style = MaterialTheme.typography.titleLarge)
                Text("Temperature ${state.temperature.toInt()}\u00B0C", color = ElectricSilk)
                Slider(state.temperature, vm::onTemp, valueRange = 10f..45f, steps = 34)
                Text("Humidity ${state.humidity.toInt()}%", color = ElectricSilk)
                Slider(state.humidity, vm::onHumidity, valueRange = 40f..95f, steps = 54)
                Text("Instar Stage", color = ElectricSilk)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    (1..5).forEach { instar ->
                        if (state.instar == instar) Button(onClick = { vm.onInstar(instar) }, colors = ButtonDefaults.buttonColors(containerColor = EcoNeon, contentColor = Color.Black)) { Text("$instar") }
                        else OutlinedButton(onClick = { vm.onInstar(instar) }) { Text("$instar") }
                    }
                }
                Text(preview.title, color = EcoNeon, style = MaterialTheme.typography.titleLarge)
                Text(preview.message, color = ElectricSilk.copy(0.75f))
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                Button(onClick = vm::save, colors = ButtonDefaults.buttonColors(containerColor = EcoNeon, contentColor = Color.Black), modifier = Modifier.fillMaxWidth()) { Text("Save Log and Advice") }
            }
        }
    }
}
