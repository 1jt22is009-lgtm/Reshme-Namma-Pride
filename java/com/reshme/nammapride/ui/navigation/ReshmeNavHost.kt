package com.reshme.nammapride.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reshme.nammapride.LocalRepository
import com.reshme.nammapride.presentation.batch.NewBatchScreen
import com.reshme.nammapride.presentation.climate.AdviceDetailScreen
import com.reshme.nammapride.presentation.climate.ClimateEntryScreen
import com.reshme.nammapride.presentation.dashboard.DashboardScreen
import com.reshme.nammapride.presentation.harvest.HarvestScreen
import com.reshme.nammapride.presentation.history.HistoryScreen
import com.reshme.nammapride.presentation.marketplace.MarketplaceScreen
import com.reshme.nammapride.presentation.settings.SettingsScreen
import com.reshme.nammapride.presentation.splash.SplashScreen
import com.reshme.nammapride.ui.theme.DeepCharcoal
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk

@Composable
fun ReshmeNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Route.Splash.value) {
        composable(Route.Splash.value) { SplashScreen { navController.navigate(Route.Dashboard.value) { popUpTo(Route.Splash.value) { inclusive = true } } } }
        composable(Route.Dashboard.value) { AppScaffold(navController) { DashboardScreen(LocalRepository.current, navController) } }
        composable(Route.NewBatch.value) { AppScaffold(navController) { NewBatchScreen(LocalRepository.current, navController) } }
        composable(Route.ClimateEntry.value) { AppScaffold(navController) { ClimateEntryScreen(LocalRepository.current, navController) } }
        composable(Route.Advice.value) { AppScaffold(navController) { AdviceDetailScreen(LocalRepository.current) } }
        composable(Route.History.value) { AppScaffold(navController) { HistoryScreen(LocalRepository.current) } }
        composable(Route.Marketplace.value) { AppScaffold(navController) { MarketplaceScreen(LocalRepository.current) } }
        composable(Route.Harvest.value) { AppScaffold(navController) { HarvestScreen(LocalRepository.current) } }
        composable(Route.Settings.value) { AppScaffold(navController) { SettingsScreen(LocalRepository.current) } }
    }
}

@Composable
private fun AppScaffold(navController: NavHostController, content: @Composable () -> Unit) {
    val items = listOf(
        Triple(Route.Dashboard, "⌂", "Home"),
        Triple(Route.ClimateEntry, "+", "Log"),
        Triple(Route.History, "≡", "History"),
        Triple(Route.Marketplace, "₹", "Market"),
        Triple(Route.Harvest, "◷", "Harvest"),
        Triple(Route.Settings, "⚙", "Settings")
    )
    val backStack by navController.currentBackStackEntryAsState()
    val current = backStack?.destination?.route
    Scaffold(
        containerColor = DeepCharcoal,
        bottomBar = {
            NavigationBar(containerColor = Color(0xF7FFFFFF), contentColor = ElectricSilk) {
                items.forEach { (route, icon, label) ->
                    NavigationBarItem(
                        selected = current == route.value,
                        onClick = { navController.navigate(route.value) { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                        icon = { Text(icon) },
                        label = { Text(label) },
                        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(selectedIconColor = Color.White, selectedTextColor = EcoNeon, indicatorColor = EcoNeon, unselectedIconColor = ElectricSilk.copy(0.62f), unselectedTextColor = ElectricSilk.copy(0.62f))
                    )
                }
            }
        }
    ) { padding -> androidx.compose.foundation.layout.Box(Modifier.padding(padding).background(DeepCharcoal)) { content() } }
}
