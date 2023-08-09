package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.FadeInFadeOutAnimatedVisibility
import com.anxops.bkn.ui.shared.getColorForStatus
import com.anxops.bkn.util.formatAsMonthYear
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun UpcomingMaintenances(
    bike: Bike? = null,
    onClickItem: (Maintenance) -> Unit = {}
) {
    val items = (bike?.componentList()?.flatMap { it.maintenances ?: emptyList() } ?: emptyList()).filter {
        it.statusLevel() >= StatusLevel.WARN
    }.sortedByDescending { it.status }

    FadeInFadeOutAnimatedVisibility(visible = items.isNotEmpty()) {
        if (items.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 30.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = if (items.isNotEmpty()) {
                        stringResource(R.string.upcoming_maintenance_title)
                    } else {
                        stringResource(
                            R.string.upcoming_maintenance_empty_title
                        )
                    },
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.forEachIndexed { index, item ->

//                        BikeComponentDetailMaintenance(item = item)

                        UpcomingMaintenanceItem(item, onClickItem = onClickItem)

                        if (index != items.lastIndex) {
                            Divider(
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpcomingMaintenanceItem(item: Maintenance, onClickItem: (Maintenance) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { onClickItem(item) },
        verticalAlignment = Alignment.Top
    ) {
        BikeComponentIcon(
            type = item.componentType,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(top = 6.dp).size(42.dp).clip(CircleShape)
                .background(MaterialTheme.colors.surface.copy(alpha = .9f)) // getColorForProgress(percentage = item.percentage)
                .padding(6.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = stringResource(id = item.type.resources().nameResId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.weight(1f).padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "~ ${item.displayStatus()}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.background(
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    ).padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {
                LinearProgressIndicator(
                    progress = item.status.toFloat(),
                    modifier = Modifier.padding(vertical = 6.dp).height(5.dp).clip(RoundedCornerShape(20.dp)).weight(1.0f),
                    color = getColorForStatus(StatusLevel.from(item.status)),
                    backgroundColor = MaterialTheme.colors.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BknIcon(
                    icon = CommunityMaterial.Icon3.cmd_repeat,
                    modifier = Modifier.padding(end = 10.dp).size(20.dp)
                )
                Text(
                    text = "${item.defaultFrequency.displayText()}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BknIcon(
                    icon = CommunityMaterial.Icon3.cmd_wrench_clock,
                    modifier = Modifier.padding(end = 10.dp).size(20.dp)
                )
                Text(
                    text = stringResource(R.string.maintenance_estimated_date, item.estimatedDate?.formatAsMonthYear() ?: "--"),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            }
        }
    }
}
