package com.reshme.nammapride.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reshme.nammapride.domain.model.ClimateStatus
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricAmber
import com.reshme.nammapride.ui.theme.ElectricSilk
import com.reshme.nammapride.ui.theme.NeonRed
import com.reshme.nammapride.ui.theme.PanelBlack

@Composable
fun GlassCard(modifier: Modifier = Modifier, accent: Color = EcoNeon, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(30.dp))
            .background(Brush.linearGradient(listOf(PanelBlack, Color(0xCFE8FFF0))))
            .border(1.dp, accent.copy(alpha = 0.58f), RoundedCornerShape(30.dp))
            .padding(20.dp),
        content = content
    )
}

@Composable
fun ScreenTitle(title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 10.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium, color = ElectricSilk)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = ElectricSilk.copy(alpha = 0.68f))
    }
}

@Composable
fun ClimateDial(temperature: Float, humidity: Float, status: ClimateStatus, modifier: Modifier = Modifier) {
    val color = when (status) { ClimateStatus.Optimal -> EcoNeon; ClimateStatus.Warning -> ElectricAmber; ClimateStatus.Danger -> NeonRed }
    val pulse by rememberInfiniteTransition(label = "dialPulse").animateFloat(0.72f, 1f, infiniteRepeatable(tween(1300), RepeatMode.Reverse), label = "pulse")
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.matchParentSize()) {
            val radius = size.minDimension / 2.7f
            val center = Offset(size.width / 2, size.height / 2)
            drawCircle(color.copy(alpha = 0.10f * pulse), radius * 1.18f, center)
            drawCircle(Color.Black.copy(alpha = 0.08f), radius, center, style = Stroke(18.dp.toPx(), cap = StrokeCap.Round))
            val progress = ((temperature - 10f) / 35f).coerceIn(0f, 1f)
            drawArc(color.copy(alpha = 0.95f), -215f, 250f * progress, false, topLeft = Offset(center.x - radius, center.y - radius), size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2), style = Stroke(18.dp.toPx(), cap = StrokeCap.Round))
            repeat(42) { index ->
                val angle = Math.toRadians((-215 + index * (250.0 / 41)).toDouble())
                val inner = radius + 18.dp.toPx()
                val outer = inner + if (index % 7 == 0) 13.dp.toPx() else 7.dp.toPx()
                drawLine(color.copy(alpha = if (index % 7 == 0) 0.85f else 0.38f), Offset(center.x + kotlin.math.cos(angle).toFloat() * inner, center.y + kotlin.math.sin(angle).toFloat() * inner), Offset(center.x + kotlin.math.cos(angle).toFloat() * outer, center.y + kotlin.math.sin(angle).toFloat() * outer), 2.dp.toPx(), StrokeCap.Round)
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${temperature.toInt()}C", color = ElectricSilk, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold)
            Text("${humidity.toInt()}% RH", color = color, style = MaterialTheme.typography.titleLarge)
            Text(status.name.uppercase(), color = ElectricSilk.copy(alpha = 0.72f), style = MaterialTheme.typography.labelLarge)
        }
    }
}
