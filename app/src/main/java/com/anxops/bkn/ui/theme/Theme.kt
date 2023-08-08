package com.anxops.bkn.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    error = md_theme_light_error,
    onError = md_theme_light_onError,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    surface = md_theme_light_surface,
    onSurface = md_theme_light_onSurface,
    primaryVariant = md_theme_light_primary_variant,
    secondaryVariant = md_theme_light_secondary_variant
)

private val DarkColorPalette = darkColors(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    error = md_theme_dark_error,
    onError = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    primaryVariant = md_theme_dark_primary_variant,
    secondaryVariant = md_theme_dark_secondary_variant
)

@Composable
fun BikenanceAndroidTheme(
    useSystemUIController: Boolean = true,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    if (useSystemUIController) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = colors.primaryVariant
        )
        systemUiController.setNavigationBarColor(
            color = colors.primaryVariant
        )
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
