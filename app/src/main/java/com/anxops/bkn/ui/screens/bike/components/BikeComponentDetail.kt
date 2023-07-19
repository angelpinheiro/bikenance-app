package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatAsMonthYear

@Composable
fun BikeComponentDetail(component: BikeComponent) {


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.Top
    ) {

        BikeComponentIcon(
            type = component.type,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(top = 6.dp)
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.surface.copy(alpha = .9f))
                .padding(6.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {

            Row(
                Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

            ) {

                Text(
                    text = "${if(component.modifier != null) "${component.modifier.displayName} " else ""}" + stringResource(component.type.resources().nameResId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colors.primaryVariant),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ) {
                BikeStat(title = "Distance", value = "${component.displayDistance()}")
                BikeStat(title = "Hours", value = "${component.displayDuration()}")
            }

//            Text(
//                text = "Maintenances",
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h2,
//                overflow = TextOverflow.Ellipsis,
//                maxLines = 1
//            )

            component.maintenances?.forEach {
                BikeComponentDetailMaintenance(item = it)
            }
        }
    }
}


@Composable
fun BikeComponentDetailMaintenance(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 6.dp)
    ) {

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

            CircularProgressIndicator(
                progress = item.status.toFloat(),
                color = getColorForStatus(StatusLevel.from(item.status)),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(22.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant,
                strokeWidth = 6.dp
            )

        }

        Text(
            text = stringResource(id = item.type.resources().descriptionResId),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        Text(
            text = " - Current wear ${(item.status*100).toInt()}%",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
        )

        Text(
            text = " - Last maintenance: ${item.lastMaintenanceDate?.formatAsMonthYear()}",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
        )

        Text(
            text = " - Estimated maintenance: ${item.estimatedDate?.formatAsMonthYear()}",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3,
        )
    }
}