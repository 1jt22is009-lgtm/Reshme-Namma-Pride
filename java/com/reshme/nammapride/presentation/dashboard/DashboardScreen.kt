package com.reshme.nammapride.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.reshme.nammapride.domain.model.ClimateStatus
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.ClimateDial
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.navigation.Route
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricAmber
import com.reshme.nammapride.ui.theme.ElectricSilk
import com.reshme.nammapride.util.DateTools

@Composable
fun DashboardScreen(repository: ReshmeRepository, navController: NavHostController) {
    val vm: DashboardViewModel = viewModel(factory = RepositoryViewModelFactory { DashboardViewModel(repository) })
    val state by vm.state.collectAsState()
    val latest = state.latest
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        ScreenTitle("Rearing Command", "Live local supervision for Karnataka sericulture")
        ClimateDial(latest?.temperatureC ?: 26f, latest?.humidity ?: 78f, latest?.status ?: ClimateStatus.Optimal, Modifier.size(280.dp))
        GlassCard(Modifier.fillMaxWidth().padding(18.dp), accent = latest?.let { when (it.status) { ClimateStatus.Optimal -> EcoNeon; ClimateStatus.Warning -> ElectricAmber; ClimateStatus.Danger -> MaterialTheme.colorScheme.error } } ?: EcoNeon) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(state.batch?.breedName ?: "No active batch", style = MaterialTheme.typography.titleLarge, color = ElectricSilk)
                Text(state.batch?.let { "Batch ${it.batchId} started ${DateTools.epochToText(it.startDateEpochDay)}" } ?: "Create a rearing batch to begin climate tracking.", color = ElectricSilk.copy(0.72f))
                LinearProgressIndicator(progress = { state.successRate / 100f }, color = EcoNeon, trackColor = ElectricSilk.copy(0.12f), modifier = Modifier.fillMaxWidth())
                Text("Green-zone success rate: ${state.successRate}%", color = EcoNeon)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { navController.navigate(Route.ClimateEntry.value) }, colors = ButtonDefaults.buttonColors(containerColor = EcoNeon, contentColor = Color.Black)) { Text("Quick Log") }
                    OutlinedButton(onClick = { navController.navigate(Route.NewBatch.value) }) { Text("New Batch") }
                }
            }
        }
        latest?.let {
            GlassCard(Modifier.fillMaxWidth().padding(horizontal = 18.dp), accent = EcoNeon) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(it.adviceTitle, style = MaterialTheme.typography.titleLarge, color = ElectricSilk)
                    Text(it.adviceMessage, color = ElectricSilk.copy(0.78f))
                    Spacer(Modifier.height(2.dp))
                    Button(onClick = { navController.navigate(Route.Advice.value) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = EcoNeon)) { Text("Open Actionable Alerts") }
                }
            }
        }
    }
}
