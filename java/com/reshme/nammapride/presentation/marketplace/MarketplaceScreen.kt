package com.reshme.nammapride.presentation.marketplace

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reshme.nammapride.domain.model.SilkVariety
import com.reshme.nammapride.domain.repository.ReshmeRepository
import com.reshme.nammapride.presentation.RepositoryViewModelFactory
import com.reshme.nammapride.ui.components.GlassCard
import com.reshme.nammapride.ui.components.ScreenTitle
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricAmber
import com.reshme.nammapride.ui.theme.ElectricSilk

@Composable
fun MarketplaceScreen(repository: ReshmeRepository) {
    val vm: MarketplaceViewModel = viewModel(factory = RepositoryViewModelFactory { MarketplaceViewModel(repository) })
    val state by vm.state.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ScreenTitle("Silk Market", "Real-time price index | ರೇಷ್ಮೆ ದರ ಸೂಚ್ಯಂಕ")

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EcoNeon)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp, 0.dp, 16.dp, 24.dp)
            ) {
                items(state.varieties) { variety ->
                    VarietyPriceCard(variety)
                }
            }
        }
    }
}

@Composable
private fun VarietyPriceCard(variety: SilkVariety) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = variety.name,
                        color = ElectricSilk,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    PriceChangeIndicator(label = "Today", change = variety.dailyChange)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${variety.currentPrice.toInt()}",
                        color = EcoNeon,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                    Text("per KG", color = ElectricSilk.copy(0.6f), fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Line Chart
            SilkPriceChart(
                history = variety.history.map { it.price },
                color = if (variety.dailyChange >= 0) EcoNeon else ElectricAmber
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ChangeSummaryBox("Monthly", variety.monthlyChange)
                ChangeSummaryBox("Yearly", variety.yearlyChange)
            }
        }
    }
}

@Composable
private fun PriceChangeIndicator(label: String, change: Float) {
    val color = if (change >= 0) EcoNeon else ElectricAmber
    val icon = if (change >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = "$label ${if (change >= 0) "+" else ""}$change%",
            color = color,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ChangeSummaryBox(label: String, change: Float) {
    val color = if (change >= 0) EcoNeon else ElectricAmber
    Column(
        modifier = Modifier
            .background(color.copy(0.1f), RoundedCornerShape(8.dp))
            .padding(8.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = ElectricSilk.copy(0.7f), fontSize = 10.sp)
        Text(
            text = "${if (change >= 0) "+" else ""}$change%",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SilkPriceChart(history: List<Float>, color: Color) {
    if (history.isEmpty()) return

    val minPrice = history.minOrNull() ?: 0f
    val maxPrice = history.maxOrNull() ?: 1f
    val range = (maxPrice - minPrice).coerceAtLeast(1f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val width = size.width
        val height = size.height
        val stepX = width / (history.size - 1)

        val points = history.mapIndexed { index, price ->
            Offset(
                x = index * stepX,
                y = height - ((price - minPrice) / range * height)
            )
        }

        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.forEach { lineTo(it.x, it.y) }
        }

        // Draw Area Fill
        val fillPath = Path().apply {
            addPath(path)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // Draw Line
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
