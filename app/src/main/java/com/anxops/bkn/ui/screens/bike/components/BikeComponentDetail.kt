package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.ui.screens.maintenances.getColorForProgress
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsMonthYear
import com.anxops.bkn.util.formatElapsedTimeUntilNow
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial


@Composable
fun BikeComponentDetailBg(): Brush {

    val color1 = MaterialTheme.colors.primary
    val color2 = MaterialTheme.colors.primaryVariant

    val gradient = remember {
        Brush.verticalGradient(
            0f to color1,
            0.6f to color1,
            1f to color2.copy(alpha = 0.3f),
        )
    }

    return gradient
}

@Composable
fun BikeComponentDetail(component: BikeComponent, onClose: () -> Unit = {}) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .background(BikeComponentDetailBg())
            .padding(16.dp)
    ) {

        Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

        ) {

            BikeComponentIcon(
                type = component.type,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.surface.copy(alpha = .9f))
                    .padding(5.dp)
            )

            Text(
                text = "${if (component.modifier != null) "${component.modifier.displayName} " else ""}" + stringResource(
                    component.type.resources().nameResId
                ),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            IconButton(onClick = { onClose() }) {
                BknIcon(icon = CommunityMaterial.Icon.cmd_close)
            }

        }

        Row(
            Modifier
                .padding(top = 0.dp)
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            BikeStat(title = "Distance", value = "${component.displayDistance()}")
            BikeStat(title = "Duration", value = "${component.from?.formatElapsedTimeUntilNow()}")
            BikeStat(title = "Active hours", value = "${component.displayDuration()}")
        }

        component.maintenances?.forEach {
            BikeComponentDetailMaintenance(item = it)
        }
    }
}


@Composable
fun BikeComponentDetailMaintenance(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        val expanded = remember {
            mutableStateOf(false)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Text(
                text = stringResource(id = item.type.resources().nameResId),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .weight(1f)
            )

        }

        LinearProgressIndicator(
            progress = item.status.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(20.dp)),
            color = getColorForProgress(percentage = item.status.toFloat()),
            backgroundColor = MaterialTheme.colors.primaryVariant,
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_repeat,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
            )
            Text(
                text = "${item.defaultFrequency.displayText()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_progress_wrench,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
            )
            Text(
                text = "Current wear ${item.displayStatus()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
            )
        }


        Row(verticalAlignment = Alignment.CenterVertically) {
            BknIcon(
                icon = CommunityMaterial.Icon.cmd_calendar_end,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
            )
            Text(
                text = "Last maintenance: ${item.lastMaintenanceDate?.formatAsMonthYear()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_wrench_clock,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp)
            )
            Text(
                text = "Next maintenance: ${item.estimatedDate?.formatAsMonthYear()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
            )
        }


        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f))
                .padding(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                BknIcon(
                    icon = CommunityMaterial.Icon3.cmd_wrench_check,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(20.dp)
                )
                Text(
                    text = "Instructions",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3,
                )
            }

            Text(
                text = stringResource(id = item.type.resources().descriptionResId),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
            )
        }


    }
}