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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.VirtualComponent
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatDistanceAsKm

data class VirtualComponentData(val virtualComponent: VirtualComponent, val components: List<BikeComponent>)

@Composable
fun VirtualComponentListItem(data: VirtualComponentData) {

    val type = data.components.firstOrNull()?.type ?: ComponentTypes.UNKNOWN

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {

        BikeComponentIcon(
            type = type, tint = MaterialTheme.colors.onSurface,
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
                    text = data.virtualComponent.description + " (x${data.components.size})",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            data.components.forEach { component ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .padding(start = 10.dp)
                        .background(MaterialTheme.colors.primary)
                ) {

//                    BikeComponentIcon(
//                        type = type, tint = MaterialTheme.colors.onSurface,
//                        modifier = Modifier
//                            .padding(top = 6.dp)
//                            .size(32.dp)
//                            .clip(CircleShape)
//                            .background(MaterialTheme.colors.surface.copy(alpha = .9f))
//                            .padding(6.dp)
//                    )

                    Column(Modifier.padding(start = 6.dp)) {


                        Text(
                            text = component.alias ?: "",
                            color = MaterialTheme.colors.background,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(start = 0.dp),
                            fontWeight = FontWeight.Bold
                        )

//                        Text(
//                            text = "Last maintenance: 3 months ago",
//                            color = MaterialTheme.colors.background,
//                            style = MaterialTheme.typography.h4,
//                            modifier = Modifier.padding(start = 0.dp)
//                        )

                        Text(
                            text = "Usage: ${formatDistanceAsKm(component.usage.km.toInt())} / ${component.usage.hours.toInt()} hours",
                            color = MaterialTheme.colors.background,
                            style = MaterialTheme.typography.h4,
                        )
                    }
                }
            }
        }

    }
}