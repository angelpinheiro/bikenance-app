package com.anxops.bkn.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver


val BknGrey = Color(0xFFACACAC)

val BknRed = Color(0xFFd32f2f)
val BknOrange = Color(0xFFf57c00)
val BknGreen = Color(0xFF7cb342)
val BknBlue = Color(0xFF5fa3fe)

//
//val md_theme_light_primary = Color(0xFF83C5BE)
//val md_theme_light_primary_variant = Color(0xFF006D77)
//val md_theme_light_secondary = Color(0xFFE29578)
//val md_theme_light_secondary_variant = Color(0xFFFFDDD2)
//val md_theme_light_background = Color(0xFFEDF6F9)
//val md_theme_light_surface = Color(0xFFFFDDD2)
//val md_theme_light_error = Color(0xFFB3261E)
//val md_theme_light_onPrimary = Color(0xFFFFFFFF)
//val md_theme_light_onSecondary = Color(0x66111111)
//val md_theme_light_onBackground = Color(0xFF222222)
//val md_theme_light_onSurface = Color(0xFF006D77)
//val md_theme_light_onError = Color(0xFFFFFFFF)


val md_theme_light_primary = Color(0xFF3A7CA5)
val md_theme_light_primary_variant = Color(0xFF16425B)
val md_theme_light_secondary = Color(0xFF2F6690)
val md_theme_light_secondary_variant = Color(0xAAEAC4D5)
val md_theme_light_background = Color(0xFFD9DCD6)
val md_theme_light_surface = Color(0xFFFFFFFF)
val md_theme_light_error = Color(0xFFB3261E)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_onSecondary = Color(0x66111111)
val md_theme_light_onBackground = Color(0xFF222222)
val md_theme_light_onSurface = Color(0xFF16425B)
val md_theme_light_onError = Color(0xFFFFFFFF)


val md_theme_dark_primary = Color(0xFF282e41)
val md_theme_dark_primary_variant = Color(0xFF181B25)
val md_theme_dark_onPrimary = Color(0xFFFFFFFF)
val md_theme_dark_secondary = Color(0xFF5fa3fe)
val md_theme_dark_secondary_variant = Color(0xAAeeeef5)
val md_theme_dark_onSecondary = Color(0xFFFFFFFF)
val md_theme_dark_error = Color(0xFFB3261E)
val md_theme_dark_onError = Color(0xFFFFFFFF)
val md_theme_dark_background = Color(0xFF181B25)
val md_theme_dark_onBackground = Color(0xDDeeeef5)
val md_theme_dark_surface = Color(0xFFFFFFFF)
val md_theme_dark_onSurface = Color(0xFF282e41)

val Colors.backgroundBox: Color
    @Composable get() = if (isLight) md_theme_light_primary_variant.copy(alpha = 0.3f)
        .compositeOver(Color.White) else md_theme_dark_background


val Colors.strava: Color
    @Composable get() = BknOrange

val Colors.onStrava: Color
    @Composable get() = Color.White

val Colors.surfaceShadow: Color
    @Composable get() = primary

val Colors.statusDanger: Color
    @Composable get() = BknRed

val Colors.statusWarning: Color
    @Composable get() = BknOrange

val Colors.statusOk: Color
    @Composable get() = BknBlue

val Colors.statusGood: Color
    @Composable get() = BknGreen