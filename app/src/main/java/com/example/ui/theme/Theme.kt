package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoogleBlueDark,
    onPrimary = Color(0xFF002D6F),
    primaryContainer = Color(0xFF174EA6),
    onPrimaryContainer = Color(0xFFD2E3FC),
    secondary = GoogleGreenDark,
    onSecondary = Color(0xFF003919),
    secondaryContainer = Color(0xFF0F5223),
    onSecondaryContainer = Color(0xFFCEEAD6),
    tertiary = GoogleYellowDark,
    onTertiary = Color(0xFF3F2E00),
    tertiaryContainer = Color(0xFF5A4300),
    onTertiaryContainer = Color(0xFFFEF7E0),
    error = GoogleRedDark,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

private val LightColorScheme = lightColorScheme(
    primary = GoogleBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2E3FC),
    onPrimaryContainer = Color(0xFF041E49),
    secondary = GoogleGreen,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCEEAD6),
    onSecondaryContainer = Color(0xFF072711),
    tertiary = Color(0xFFE37400),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFEF7E0),
    onTertiaryContainer = Color(0xFF422C00),
    error = GoogleRed,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
