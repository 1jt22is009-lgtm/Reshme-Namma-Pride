package com.reshme.nammapride.presentation.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricAmber
import com.reshme.nammapride.ui.theme.ElectricSilk
import com.reshme.nammapride.ui.theme.NeonRed
import com.reshme.nammapride.util.DateTools

@Composable
fun HistoryScreen(repository: ReshmeRepository) {
    val vm: HistoryViewModel = viewModel(factory = RepositoryViewModelFactory { HistoryViewModel(repository) })
    val state by vm.state.collectAsState()
    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Batch History", "Scrollable climate timeline and rearing memory")
        LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(18.dp)) {
            if (state.logs.isEmpty()) item { GlassCard(Modifier.fillMaxWidth()) { Text("No logs recorded yet.", color = ElectricSilk) } }
            items(state.logs, key = { it.id }) { log ->
                val accent = when (log.status) { ClimateStatus.Optimal -> EcoNeon; ClimateStatus.Warning -> ElectricAmber; ClimateStatus.Danger -> NeonRed }
                GlassCard(Modifier.fillMaxWidth(), accent = accent) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(log.status.name, color = accent, style = MaterialTheme.typography.titleLarge)
                            Text("Instar ${log.instar}", color = ElectricSilk.copy(0.7f))
                        }
                        Text("Batch ${log.batchId}", color = ElectricSilk)
                        Text("${log.temperatureC.toInt()}\u00B0C  |  ${log.humidity.toInt()}% RH", color = ElectricSilk)
                        Text(log.adviceTitle, color = ElectricSilk.copy(0.82f))
                        Text(DateTools.millisToText(log.createdAtMillis), color = ElectricSilk.copy(0.58f))
                    }
                }
            }
        }
    }
}
