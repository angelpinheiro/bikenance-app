package com.anxops.bkn.ui.screens.maintenances.components

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.data.model.RevisionUnit
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.onBackgroundTextFieldColors

@Composable
fun RevisionFreqEdit(frequency: RevisionFrequency) {

    val unit by remember(frequency) {
        mutableStateOf(frequency.unit)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {

        Text(text = "Service by " + unit.name, color = MaterialTheme.colors.onBackground)

        when (unit) {
            RevisionUnit.KILOMETERS -> RevisionFrequencyDistanceEdit(frequency)
            RevisionUnit.HOURS -> RevisionFrequencyHoursEdit(frequency)
            RevisionUnit.MONTHS, RevisionUnit.WEEKS, RevisionUnit.YEARS -> RevisionFrequencyIntervalEdit(
                frequency
            )
        }
    }

}

@Composable
fun RevisionFrequencyIntervalEdit(
    frequency: RevisionFrequency,
    onChange: (RevisionFrequency) -> Unit = {}
) {
    val colors = onBackgroundTextFieldColors()
    BknLabelTopTextField(
        label = "Every ${frequency.every} ${frequency.unit}",
        value = frequency.every.toString(),
        colors = colors,
        onValueChange = {
            frequency.copy(every = it.toInt())
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun RevisionFrequencyHoursEdit(frequency: RevisionFrequency) {
    Text("TODO", color = MaterialTheme.colors.onBackground)
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { numberPicker, i, i2 ->  }
                minValue = 0
                maxValue = 50
            }
        }
    )
}

@Composable
fun RevisionFrequencyDistanceEdit(frequency: RevisionFrequency) {
    Text("TODO", color = MaterialTheme.colors.onBackground)
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            NumberPicker(context).apply {
                setOnValueChangedListener { numberPicker, i, i2 ->  }
                minValue = 0
                maxValue = 50
            }
        }
    )
}
