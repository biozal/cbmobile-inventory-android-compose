package com.cbmobile.inventory.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Red600,
    primaryVariant = Red300,
    onPrimary = Color.White,
    secondary = Red200,
    secondaryVariant = Red100,
    onSecondary = Color.Black,
    background = BackgroundDark,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Red600,
    primaryVariant = Red300,
    onPrimary = Color.White,
    secondary = Red200,
    secondaryVariant = Red100,
    onSecondary = Color.Black,
    background = BackgroundLight,
    onSurface = Color.Black

    /* Other default colors to override
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun InventoryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}