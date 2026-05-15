package com.reshme.nammapride.presentation.climate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reshme.nammapride.domain.model.ClimateStatus
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.presentation.dashboard.DashboardViewModel
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricAmber
import com.reshme.nammapride.ui.theme.ElectricSilk
import com.reshme.nammapride.ui.theme.NeonRed

@Composable
fun AdviceDetailScreen(repository: ReshmeRepository) {
    val vm: DashboardViewModel = viewModel(factory = RepositoryViewModelFactory { DashboardViewModel(repository) })
    val state by vm.state.collectAsState()
    val latest = state.latest
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        ScreenTitle("Actionable Alerts", "Offline expert guidance for the current Instar")
        if (latest == null) {
            GlassCard(Modifier.fillMaxWidth().padding(18.dp)) { Text("No climate log yet. Add temperature and humidity readings to generate advice.", color = ElectricSilk) }
        } else {
            val accent = when (latest.status) { ClimateStatus.Optimal -> EcoNeon; ClimateStatus.Warning -> ElectricAmber; ClimateStatus.Danger -> NeonRed }
            GlassCard(Modifier.fillMaxWidth().padding(18.dp), accent = accent) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    Text(latest.status.name.uppercase(), color = accent, style = MaterialTheme.typography.headlineMedium)
                    Text(latest.adviceTitle, color = ElectricSilk, style = MaterialTheme.typography.headlineMedium)
                    Text(latest.adviceMessage, color = ElectricSilk, style = MaterialTheme.typography.bodyLarge)
                    Text("Instar ${latest.instar} climate: ${latest.temperatureC.toInt()}\u00B0C and ${latest.humidity.toInt()}% humidity", color = ElectricSilk.copy(0.72f))
                    Text("Field protocol: adjust ventilation first, correct moisture second, and avoid sudden shock to larvae.", color = ElectricSilk.copy(0.72f))
                }
            }
        }
    }
}
