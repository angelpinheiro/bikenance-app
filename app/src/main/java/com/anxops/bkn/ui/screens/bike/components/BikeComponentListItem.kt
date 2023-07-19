package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatAsDayMonth


@Composable
fun BikeComponentListItem(component: BikeComponent) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {

        BikeComponentIcon(
            type = component.type, tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(top = 6.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.surface.copy(alpha = .9f)) //getColorForProgress(percentage = item.percentage)
                .padding(6.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {

                Text(
                    text = stringResource(component.type.resources().nameResId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

            }

//            Row(verticalAlignment = Alignment.CenterVertically) {
//
//
//                Text(
//                    text = "Last maintenance: ${component.from?.formatAsDayMonth() ?: "--"}",
//                    color = MaterialTheme.colors.background,
//                    style = MaterialTheme.typography.h4,
//                    modifier = Modifier.padding(start = 0.dp)
//                )
//            }


            Text(
                text = "${component.displayDistance()} / ${component.displayDuration()}",
                color = MaterialTheme.colors.background,
                style = MaterialTheme.typography.h3,
            )

            component.maintenances?.forEach {
                MaintenanceItemSummary(item = it)
            }

        }


    }
}