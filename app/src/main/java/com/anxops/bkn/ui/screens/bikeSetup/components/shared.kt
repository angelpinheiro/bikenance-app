package com.anxops.bkn.ui.screens.bikeSetup.components

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BooleanRadioButton(labelTrue: String, labelFalse: String, value: Boolean = false) {
    val colors = RadioButtonDefaults.colors(
        unselectedColor = MaterialTheme.colors.surface,
        selectedColor = MaterialTheme.colors.secondary
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        Text(
            text = labelTrue,
            color = MaterialTheme.colors.surface
        )
        RadioButton(
            selected = value,
            onClick = { },
            colors = colors
        )
        Text(
            text = labelFalse,
            color = MaterialTheme.colors.surface
        )
        RadioButton(
            selected = !value,
            onClick = { },
            colors = colors
        )
    }
}

@Composable
fun BooleanSelector(
    labelTrue: String,
    labelFalse: String,
    value: Boolean? = null,
    onOptionSelected: (Boolean) -> Unit
) {
    val colors = RadioButtonDefaults.colors(
        unselectedColor = MaterialTheme.colors.surface,
        selectedColor = MaterialTheme.colors.secondary
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = {
                onOptionSelected(true)
            },
            modifier = Modifier.padding(4.dp).weight(1f),
                colors = if (value == true) {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            } else {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant
                )
            }
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = labelTrue,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h5

            )
        }

        OutlinedButton(
            onClick = {
                onOptionSelected(false)
            },
            modifier = Modifier.padding(4.dp).weight(1f),
                colors = if (value == false) {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            } else {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant
                )
            }
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = labelFalse,
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h5

            )
        }
    }
}

@Composable
fun selectableButton(text: String, selected: Boolean, onSelected: () -> Unit = {}) {
    OutlinedButton(
        onClick = {
            onSelected()
        },
        modifier = Modifier.padding(4.dp).fillMaxWidth(0.8f),
            colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary,
            style = MaterialTheme.typography.h5
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    value: String,
    onValueChange: (String) -> Unit = {},
    pattern: String = "yyyy-MM-dd",
    visible: Boolean = false
) {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val date = if (value.isNotBlank()) LocalDate.parse(value, formatter) else LocalDate.now()
    val dialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            onValueChange(LocalDate.of(year, month + 1, dayOfMonth).toString())
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth
    )

    LaunchedEffect(visible) {
        if (visible) {
            dialog.show()
        }
    }
}
