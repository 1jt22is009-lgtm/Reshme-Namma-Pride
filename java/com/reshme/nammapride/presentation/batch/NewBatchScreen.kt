package com.reshme.nammapride.presentation.batch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.navigation.Route
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk

@Composable
fun NewBatchScreen(repository: ReshmeRepository, navController: NavHostController) {
    val context = LocalContext.current
    val vm: NewBatchViewModel = viewModel(factory = RepositoryViewModelFactory { NewBatchViewModel(repository) })
    val state by vm.state.collectAsState()
    LaunchedEffect(state.saved) { if (state.saved) navController.navigate(Route.Dashboard.value) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        ScreenTitle("New Batch", "Initialize breed, identity, and harvest countdown")
        GlassCard(Modifier.fillMaxWidth().padding(18.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(state.breed, vm::onBreed, label = { Text("Breed Name") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(state.batchId, vm::onBatchId, label = { Text("Batch ID") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(state.startDate, vm::onStartDate, label = { Text("Start Date (YYYY-MM-DD)") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                if (state.saved) Text("Batch saved. Harvest alert scheduled.", color = EcoNeon)
                Button(onClick = { vm.save(context) }, colors = ButtonDefaults.buttonColors(containerColor = EcoNeon, contentColor = Color.Black), modifier = Modifier.fillMaxWidth()) { Text("Start Rearing Cycle") }
                Text("Recommended start date is the brushing date. Existing active batches are archived automatically.", color = ElectricSilk.copy(0.62f))
            }
        }
    }
}
