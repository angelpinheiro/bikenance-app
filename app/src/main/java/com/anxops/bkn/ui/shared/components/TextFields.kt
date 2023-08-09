package com.anxops.bkn.ui.shared.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

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
