package com.reshme.nammapride.presentation.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reshme.nammapride.ui.theme.DeepCharcoal
import com.reshme.nammapride.ui.theme.EcoNeon
import com.reshme.nammapride.ui.theme.ElectricSilk
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    val pulse by rememberInfiniteTransition(label = "silkPulse").animateFloat(0.65f, 1.25f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "pulse")
    LaunchedEffect(Unit) { delay(1800); onDone() }
    Column(Modifier.fillMaxSize().background(DeepCharcoal), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Canvas(Modifier.size(190.dp)) {
            val path = Path().apply {
                moveTo(size.width * 0.14f, size.height * 0.62f)
                cubicTo(size.width * 0.28f, size.height * 0.08f, size.width * 0.72f, size.height * 0.08f, size.width * 0.86f, size.height * 0.62f)
                cubicTo(size.width * 0.68f, size.height * 0.42f, size.width * 0.32f, size.height * 0.42f, size.width * 0.14f, size.height * 0.62f)
            }
            drawCircle(EcoNeon.copy(alpha = 0.12f * pulse), size.minDimension * 0.48f, Offset(size.width / 2, size.height / 2))
            drawPath(path, ElectricSilk.copy(alpha = 0.95f), style = Stroke(7.dp.toPx()))
            drawPath(path, EcoNeon.copy(alpha = 0.72f), style = Stroke(2.dp.toPx()))
            drawCircle(EcoNeon, 11.dp.toPx(), Offset(size.width / 2, size.height * 0.62f))
        }
        Text("Reshme", color = ElectricSilk, fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
        Text("NAMMA PRIDE", color = EcoNeon, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text("Sericulture climate supervisor", color = ElectricSilk.copy(0.62f), fontSize = 14.sp)
    }
}
