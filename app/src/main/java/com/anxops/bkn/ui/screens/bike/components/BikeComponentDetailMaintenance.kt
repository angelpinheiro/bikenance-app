package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.getColorForProgress
import com.anxops.bkn.util.formatAsMonthYear
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun BikeComponentDetailMaintenance(item: Maintenance, onSelected: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable {
        onSelected()
    }
    ) {
        val expanded = remember {
            mutableStateOf(false)
        }

        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                text = stringResource(id = item.type.resources().nameResId),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = 0.dp).weight(1f)
            )

//            IconButton(onClick = {onSelected() }) {
//                BknIcon(
//                    icon = CommunityMaterial.Icon3.cmd_wrench,
//                    modifier = Modifier
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colors.primaryVariant)
//                        .padding(6.dp)
//                        .size(18.dp),
//                    color = MaterialTheme.colors.onBackground
//                )
//            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_repeat,
                modifier = Modifier.padding(end = 10.dp).size(16.dp)
            )
            Text(
                text = "${item.defaultFrequency.displayText()} (${item.displayStatus()})",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3
            )
        }

        LinearProgressIndicator(
            progress = item.status.toFloat(),
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 5.dp).height(5.dp).clip(RoundedCornerShape(20.dp)),
            color = getColorForProgress(percentage = item.status.toFloat()),
            backgroundColor = MaterialTheme.colors.primaryVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${item.lastMaintenanceDate?.formatAsMonthYear()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4
            )
            Text(
                text = "~ ${item.estimatedDate?.formatAsMonthYear()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4
            )
        }

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            BknIcon(
//                icon = CommunityMaterial.Icon3.cmd_repeat,
//                modifier = Modifier
//                    .padding(end = 10.dp)
//                    .size(20.dp)
//            )
//            Text(
//                text = "${item.defaultFrequency.displayText()} (${item.displayStatus()})",
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h3,
//            )
//        }

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            BknIcon(
//                icon = CommunityMaterial.Icon3.cmd_progress_wrench,
//                modifier = Modifier
//                    .padding(end = 10.dp)
//                    .size(20.dp)
//            )
//            Text(
//                text = "Current wear ${item.displayStatus()}",
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h3,
//            )
//        }

//        Row(verticalAlignment = Alignment.CenterVertically) {
//            BknIcon(
//                icon = CommunityMaterial.Icon.cmd_calendar_end,
//                modifier = Modifier
//                    .padding(end = 10.dp)
//                    .size(20.dp)
//            )
//            Text(
//                text = "Last maintenance: ${item.lastMaintenanceDate?.formatAsMonthYear()}",
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h3,
//            )
//        }
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            BknIcon(
//                icon = CommunityMaterial.Icon3.cmd_wrench_clock,
//                modifier = Modifier
//                    .padding(end = 10.dp)
//                    .size(20.dp)
//            )
//            Text(
//                text = "Next maintenance: ${item.estimatedDate?.formatAsMonthYear()}",
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h3,
//            )
//        }
    }
}
