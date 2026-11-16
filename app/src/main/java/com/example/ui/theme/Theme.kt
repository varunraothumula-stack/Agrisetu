package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AgriDarkPrimary,
    onPrimary = Color(0xFF00390B),
    primaryContainer = AgriDarkPrimaryContainer,
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = AgriDarkSecondary,
    onSecondary = Color(0xFF4A2800),
    secondaryContainer = Color(0xFF6B3C00),
    onSecondaryContainer = Color(0xFFFFDCC1),
    background = AgriDarkBackground,
    surface = AgriDarkSurface,
    onBackground = Color(0xFFE2E3DE),
    onSurface = Color(0xFFE2E3DE),
    surfaceVariant = Color(0xFF2B322A),
    outline = Color(0xFF8C9388)
)

private val LightColorScheme = lightColorScheme(
    primary = AgriGreenLight,
    onPrimary = Color.White,
    primaryContainer = AgriGreenContainer,
    onPrimaryContainer = AgriOnGreenContainer,
    secondary = AgriAmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = AgriAmberContainer,
    onSecondaryContainer = AgriOnAmberContainer,
    background = AgriBackground,
    surface = AgriSurface,
    onBackground = AgriTextPrimary,
    onSurface = AgriTextPrimary,
    surfaceVariant = AgriSurfaceVariant,
    onSurfaceVariant = AgriTextSecondary,
    outline = AgriOutline,
    outlineVariant = AgriOutlineVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep agricultural green identity prominent across devices
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
