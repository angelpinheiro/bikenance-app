package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.resources

@Composable
fun BottomSheetComponentItem(
    type: ComponentTypes,
    selectable: Boolean = false,
    selected: Boolean = true,
    onClick: () -> Unit = {}
) {

    val resources = type.resources()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(end = 10.dp)
            .padding(vertical = 5.dp)
    ) {

        BikeComponentIcon(
            type = type,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(10.dp)
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(10.dp)
        )

        Column(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(resources.nameResId),
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(resources.descriptionResId),
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary
            )

        }
        if (selectable) {
            RadioButton(selected = selected, onClick = {
                onClick()
            })
        }
    }
}