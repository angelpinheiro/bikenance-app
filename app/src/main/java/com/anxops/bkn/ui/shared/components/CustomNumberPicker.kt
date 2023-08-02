package com.anxops.bkn.ui.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.anxops.bkn.ui.screens.bikeComponents.components.decrementBy
import com.anxops.bkn.ui.screens.bikeComponents.components.incrementBy
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomNumberPicker(
    value: Int, increment: Int = 1, suffix: String, range: IntRange, onChange: (Int) -> Unit,
) {
    val colors = onBackgroundTextFieldColors()
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        IconButton(onClick = {
            onChange(decrementBy(value, increment, range))
        }) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_minus, modifier = Modifier
                    .size(48.dp)
                    .clip(
                        RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp)
                    )
                    .background(MaterialTheme.colors.secondary)
                    .padding(6.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.surface)
        ) {
            BasicTextField(
                value = if (value == 0) "" else value.toString(),

                onValueChange = { text ->

                    if (text.isNotBlank()) {
                        val v = text.toInt()
                        val newVal = if (range.contains(v)) {
                            v
                        } else if (v < range.first) {
                            range.first
                        } else {
                            range.last
                        }
                        onChange(newVal)
                    } else {
                        onChange(0)
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = VisualTransformation.None,
            ) { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = value.toString(),
                    colors = colors,
                    singleLine = true,
                    enabled = true,
                    interactionSource = interactionSource,
                    visualTransformation = VisualTransformation.None, //SuffixTransformation(suffix = " ${suffix.lowercase()}"),
                    innerTextField = innerTextField,
                    contentPadding = PaddingValues(6.dp),
                )
            }

            Text(
                text = suffix.lowercase(),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp),
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.caption
            )
        }

        IconButton(onClick = { onChange(incrementBy(value, increment, range)) }) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_plus, modifier = Modifier
                    .size(48.dp)
                    .clip(
                        RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                    )
                    .background(MaterialTheme.colors.secondary)
                    .padding(6.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}