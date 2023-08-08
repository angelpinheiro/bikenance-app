package com.anxops.bkn.ui.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.mikepenz.iconics.typeface.IIcon

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
fun BknIcon(
    icon: IIcon,
    color: Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier.size(26.dp)
) {
    com.mikepenz.iconics.compose.Image(
        icon,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
}

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

@Composable
fun BknOutlinedTextField(
    value: String?,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    update: (String) -> Unit,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    OutlinedTextField(
        modifier = modifier,
        value = value ?: "",
        onValueChange = update,
        placeholder = {
            Text(text = label)
        },
        colors = colors,
        shape = RoundedCornerShape(6.dp),
        keyboardOptions = keyboardOptions

    )
}

@Composable
fun BknLabelTopTextField(
    value: String?,
    label: String,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = onBackgroundTextFieldColors()
) {
    TextField(
        readOnly = readOnly,
        modifier = modifier,
        value = value ?: "",
        onValueChange = onValueChange,
        placeholder = {
            Text(text = label)
        },
        colors = colors,
        shape = RoundedCornerShape(4.dp),
        keyboardOptions = keyboardOptions,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation

    )
}

@Composable
fun FadeInFadeOutAnimatedVisibility(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()

    ) {
        content()
    }
}

@Composable
fun FadeInFadeOutSlideAnimatedVisibility(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()

    ) {
        content()
    }
}

@Composable
fun SlideFromBottomAnimatedVisibility(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { 200 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { 200 })
    ) {
        content()
    }
}
