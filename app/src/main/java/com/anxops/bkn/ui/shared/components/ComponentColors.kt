package com.anxops.bkn.ui.shared.components

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun onSurfaceTextFieldColors(): TextFieldColors = TextFieldDefaults.textFieldColors(
    backgroundColor = MaterialTheme.colors.secondaryVariant,
    cursorColor = MaterialTheme.colors.onSurface,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    textColor = MaterialTheme.colors.onSurface
)

@Composable
fun onBackgroundTextFieldColors(): TextFieldColors = TextFieldDefaults.textFieldColors(
    backgroundColor = MaterialTheme.colors.primary,
    cursorColor = MaterialTheme.colors.onPrimary,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    textColor = MaterialTheme.colors.onPrimary
)

@Composable
fun secondaryButtonColors(): ButtonColors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)

@Composable
fun bgGradient(): Brush {
    val color1 = MaterialTheme.colors.primary
    val color2 = MaterialTheme.colors.primaryVariant

    val gradient = remember {
        Brush.verticalGradient(
            0f to color1.copy(alpha = 0.97f),
            0.3f to color2.copy(alpha = 0.98f),
            1f to color2.copy(alpha = 0.98f)
        )
    }

    return gradient
}
