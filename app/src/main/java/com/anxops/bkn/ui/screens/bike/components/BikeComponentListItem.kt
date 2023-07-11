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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatDistanceAsKm


@Composable
fun BikeComponentListItem(component: BikeComponent) {

    val resources = component.type.resources()

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
                    text = stringResource(resources.nameResId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
//                Text(
//                    text = "${item.title} ~ ${item.displayPercentage()}",
//                    color = MaterialTheme.colors.onPrimary,
//                    style = MaterialTheme.typography.h5,
//                    modifier = Modifier
//                        .background(
//                            color = MaterialTheme.colors.primaryVariant,
//                            shape = CircleShape
//                        )
//                        .padding(horizontal = 5.dp, vertical = 2.dp)
//                )
            }

//            Row(
//                Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//
//            ) {
//
//                LinearProgressIndicator(
//                    progress = item.percentage,
//                    modifier = Modifier
//                        .padding(vertical = 6.dp)
//                        .height(5.dp)
//                        .clip(RoundedCornerShape(20.dp))
//                        .weight(1.0f),
//                    color = getColorForProgress(percentage = item.percentage),
//                    backgroundColor = MaterialTheme.colors.primary,
//                )
//            }


            Row(verticalAlignment = Alignment.CenterVertically) {


                Text(
                    text = "Last maintenance: 3 months ago",
                    color = MaterialTheme.colors.background,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 0.dp)
                )
            }


            Text(
                text = "Usage: ${formatDistanceAsKm(component.usage.km.toInt())} / ${component.usage.hours.toInt()} hours",
                color = MaterialTheme.colors.background,
                style = MaterialTheme.typography.h4,
            )
        }


    }
}

@Composable
fun BikeComponentListItemOld(
    component: BikeComponent,
    selectable: Boolean = false,
    selected: Boolean = true,
    onClick: () -> Unit = {}
) {

    val resources = component.type.resources()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(end = 10.dp)
            .padding(vertical = 5.dp)
    ) {

        BikeComponentIcon(
            type = component.type,
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
                text = "Usage: ${formatDistanceAsKm(component.usage.km.toInt())} / ${component.usage.hours.toInt()} hours",
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = "Last maintenance: 2 months ago",
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