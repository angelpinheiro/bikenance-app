package com.anxops.bkn.ui.shared

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.ui.screens.LoginScreen
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.mikepenz.iconics.typeface.IIcon
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun onSurfaceTextFieldColors(): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.secondaryVariant,
        cursorColor = MaterialTheme.colors.onSurface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        textColor = MaterialTheme.colors.onSurface,
    )


@Composable
fun BknIcon(icon: IIcon, color: Color = MaterialTheme.colors.onPrimary, modifier: Modifier = Modifier) {

    com.mikepenz.iconics.compose.Image(
        icon,
        colorFilter = ColorFilter.tint(color),
        modifier = modifier
    )
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
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {

    TextField(
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


@Preview(showBackground = true)
@Composable
fun Prev() {
    BikenanceAndroidTheme(useSystemUIController = false) {
        LoginScreen(EmptyDestinationsNavigator)
    }
}