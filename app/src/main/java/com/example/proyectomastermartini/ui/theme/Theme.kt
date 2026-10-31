package com.example.proyectomastermartini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

private val LightColors = lightColorScheme(
    primary = MartiniGreen,
    onPrimary = Color.White,
    primaryContainer = MartiniGreenContainer,
    onPrimaryContainer = MartiniGreen,
    secondary = MartiniSecondary,
    onSecondary = Color.White,
    secondaryContainer = MartiniSurfaceVariant,
    onSecondaryContainer = MartiniTextPrimary,
    tertiary = MartiniChocolate,
    onTertiary = Color.White,
    tertiaryContainer = MartiniChocolateContainer,
    onTertiaryContainer = MartiniChocolate,
    background = MartiniCream,
    onBackground = MartiniTextPrimary,
    surface = Color.White,
    onSurface = MartiniTextPrimary,
    surfaceVariant = MartiniSurfaceVariant,
    onSurfaceVariant = MartiniTextSecondary,
    outline = MartiniOutline,
    error = MartiniError,
    onError = Color.White
)

// Tipografía algo más grande que la de Material por defecto (usuarios con dificultades visuales).
private val AppTypography = Typography(
    headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold),
    titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 18.sp, lineHeight = 26.sp),
    bodyMedium = TextStyle(fontSize = 16.sp, lineHeight = 23.sp),
    bodySmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp)
)

/** @param largeText activa la opción de accesibilidad "texto grande" (x1.25). */
@Composable
fun MasterMartiniTheme(largeText: Boolean = false, content: @Composable () -> Unit) {
    val base = LocalDensity.current
    val density = Density(base.density, base.fontScale * if (largeText) 1.25f else 1f)
    CompositionLocalProvider(LocalDensity provides density) {
        MaterialTheme(
            colorScheme = LightColors,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}
