package com.reshme.nammapride.presentation.harvest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk
import com.reshme.nammapride.util.DateTools

@Composable
fun HarvestScreen(repository: ReshmeRepository) {
    val vm: HarvestViewModel = viewModel(factory = RepositoryViewModelFactory { HarvestViewModel(repository) })
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        ScreenTitle("Harvest Tracker", "Countdown to spinning tray transfer")
        GlassCard(Modifier.fillMaxWidth().padding(18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                Text(state.batch?.breedName ?: "No active batch", color = ElectricSilk, style = MaterialTheme.typography.headlineMedium)
                Text("${state.daysLeft} days left", color = EcoNeon, style = MaterialTheme.typography.displayMedium)
                LinearProgressIndicator(progress = { state.progress }, color = EcoNeon, trackColor = ElectricSilk.copy(0.12f), modifier = Modifier.fillMaxWidth())
                Text(state.batch?.let { "Expected transfer: ${DateTools.epochToText(it.expectedHarvestEpochDay)}" } ?: "Start a batch to activate countdown.", color = ElectricSilk.copy(0.72f))
            }
        }
        GlassCard(Modifier.fillMaxWidth().padding(horizontal = 18.dp), accent = EcoNeon) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Spinning Tray Guide", color = ElectricSilk, style = MaterialTheme.typography.titleLarge)
                Text("1. Prepare clean chandrika or mountages before mature worms wander.", color = ElectricSilk.copy(0.78f))
                Text("2. Stop feeding when worms become translucent and active.", color = ElectricSilk.copy(0.78f))
                Text("3. Transfer gently in small groups and keep airflow steady.", color = ElectricSilk.copy(0.78f))
                Text("4. Remove diseased or weak larvae before mounting.", color = ElectricSilk.copy(0.78f))
            }
        }
    }
}
