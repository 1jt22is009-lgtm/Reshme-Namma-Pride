package com.reshme.nammapride.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ReshmeScheme = lightColorScheme(
    primary = EcoNeon,
    onPrimary = Color.White,
    secondary = ElectricAmber,
    onSecondary = Color.Black,
    tertiary = ElectricSilk,
    background = DeepCharcoal,
    surface = PanelBlack,
    onSurface = ElectricSilk,
    error = NeonRed,
    onError = Color.White,
    surfaceVariant = Color(0xFFE4F5E8),
    outline = Color(0xFF97B59D)
)

@Composable
fun ReshmeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ReshmeScheme, typography = ReshmeTypography, shapes = ReshmeShapes, content = content)
}
