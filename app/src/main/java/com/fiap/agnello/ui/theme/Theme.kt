package com.fiap.agnello.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val Blue = Color(0xFF007AFF)
private val BlueDark = Color(0xFF0A84FF)

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,

    secondary = Color(0xFF5856D6),
    onSecondary = Color.White,

    tertiary = Color(0xFFFF9500),
    onTertiary = Color.White,

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),

    surface = Color(0xFFF2F2F7),
    onSurface = Color(0xFF1C1C1E)
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueDark,
    onPrimary = Color.White,

    secondary = Color(0xFF5E5CE6),
    onSecondary = Color.White,

    tertiary = Color(0xFFFF9F0A),
    onTertiary = Color.Black,

    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF1C1C1E),
    onSurface = Color(0xFFF2F2F7)
)


@Composable
fun AgnneloAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
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
        content = content
    )
}