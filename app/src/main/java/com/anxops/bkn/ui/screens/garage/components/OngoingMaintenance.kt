package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.screens.maintenances.components.MaintenanceItem
import com.anxops.bkn.ui.screens.maintenances.components.getColorForProgress
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.SlideInOutAnimatedVisibility
import com.anxops.bkn.ui.theme.statusDanger

@Composable
fun OngoingMaintenance(bike: Bike) {

    val items = remember(bike) {
        FakeData.maintenances.filter { it.bike == bike.name }
    }

    SlideInOutAnimatedVisibility(visible = items.isNotEmpty()) {

        if (items.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 24.dp)
            ) {

                val title =
                    (if (items.isNotEmpty()) "Upcoming maintenance" else "No upcoming maintenance")

                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = title,
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items.forEachIndexed { index, item ->
                        OngoingMaintenanceItemV2(item)

                        if (index != items.lastIndex)
                            Divider(
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp)
                                    .height(1.dp)
                            )
                    }
                }
            }
        }

    }
}


@Composable
fun OngoingMaintenanceItemV2(item: MaintenanceItem) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {

        BikeComponentIcon(
            type = item.componentType, tint = MaterialTheme.colors.onSurface,
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
                    text = item.bikePart,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = "${item.title} ~ ${item.displayPercentage()}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.primaryVariant,
                            shape = CircleShape
                        )
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {

                LinearProgressIndicator(
                    progress = item.percentage,
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .weight(1.0f),
                    color = getColorForProgress(percentage = item.percentage),
                    backgroundColor = MaterialTheme.colors.primary,
                )
//                Text(
//                    text = item.displayPercentage(),
//                    color = MaterialTheme.colors.onPrimary,
//                    style = MaterialTheme.typography.h4,
//                    modifier = Modifier.width(60.dp).padding(horizontal = 10.dp)
//                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {


                Text(
                    text = "Last ${item.title}: 3 months ago",
                    color = MaterialTheme.colors.background,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 0.dp)
                )
            }

            if (item.percentage < 1.0) {
                Text(
                    text = "Estimated duration: 2 months",
                    color = MaterialTheme.colors.background,
                    style = MaterialTheme.typography.h4,
                )
            } else {
                Text(
                    text = "Service required",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .background(
                            color = MaterialTheme.colors.statusDanger,
                            shape = CircleShape
                        )
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                )
            }
        }


    }
}

@Composable
fun OngoingMaintenanceItem(item: MaintenanceItem) {

    Column(
        modifier = Modifier
            .padding(bottom = 1.dp)
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {

        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {

//            CircularProgressIndicator(
//                progress = item.percentage,
//                modifier = Modifier.size(26.dp),
//                strokeWidth = 6.dp,
//                color = getColorForProgress(percentage = item.percentage),
//                backgroundColor = MaterialTheme.colors.primary
//            )

            BikeComponentIcon(
                type = item.componentType, tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(getColorForProgress(percentage = item.percentage))
                    .padding(5.dp)
            )

            Text(
                text = item.bikePart,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Divider(
                color = getColorForProgress(percentage = item.percentage),
                modifier = Modifier
                    .height(1.dp)
                    .padding(end = 10.dp)
                    .weight(1f)
            )


        }
        Text(
            text = "Last ${item.title}: 3 months ago",
            color = MaterialTheme.colors.background,
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(start = 50.dp)
        )

        Text(
            text = item.title,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primaryVariant,
                    shape = CircleShape
                )
                .padding(horizontal = 10.dp, vertical = 2.dp)
        )

        if (item.percentage < 1.0) {
            Text(
                text = "Estimated duration: 2 months",
                color = MaterialTheme.colors.background,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(start = 50.dp)
            )
        } else {
            Text(
                text = "Service required",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .padding(start = 50.dp)
                    .background(
                        color = MaterialTheme.colors.statusDanger,
                        shape = CircleShape
                    )
                    .padding(horizontal = 6.dp, vertical = 1.dp)
            )
        }
    }

}