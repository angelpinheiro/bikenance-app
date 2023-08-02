package com.anxops.bkn.ui.screens.bikeComponents.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.ui.screens.maintenances.getColorForProgress
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.secondaryButtonColors
import com.anxops.bkn.util.formatAsMonthYear
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun MaintenanceDetail(item: Maintenance, onEdit: () -> Unit) {

    Card(
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.95f)
            .compositeOver(MaterialTheme.colors.surface),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(8.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = item.type.resources().nameResId),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                BknIcon(icon = CommunityMaterial.Icon.cmd_cog_refresh,
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onEdit() })
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BknIcon(
                    icon = CommunityMaterial.Icon3.cmd_repeat,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(16.dp)
                )
                Text(
                    text = "${item.defaultFrequency.displayText()} (${item.displayStatus()})",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3,
                )
            }

            LinearProgressIndicator(
                progress = item.status.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(20.dp)),
                color = getColorForProgress(percentage = item.status.toFloat()),
                backgroundColor = MaterialTheme.colors.primaryVariant,
            )


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${item.lastMaintenanceDate?.formatAsMonthYear()}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                )
                Text(
                    text = "~ ${item.estimatedDate?.formatAsMonthYear()}",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    BknIcon(CommunityMaterial.Icon3.cmd_progress_wrench)
                }
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = secondaryButtonColors(),
                ) {
                    Text("Perform service")
                }
            }

//
//        Column(
//            modifier = Modifier.padding(top = 10.dp).fillMaxWidth().clip(RoundedCornerShape(10.dp))
//                .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)).padding(10.dp)
//        ) {
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(bottom = 10.dp)
//            ) {
//                BknIcon(
//                    icon = CommunityMaterial.Icon3.cmd_wrench_check,
//                    modifier = Modifier.padding(end = 10.dp).size(20.dp)
//                )
//                Text(
//                    text = "Instructions",
//                    color = MaterialTheme.colors.onPrimary,
//                    style = MaterialTheme.typography.h3,
//                )
//            }
//
//            Text(
//                text = stringResource(id = item.type.resources().descriptionResId),
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h3,
//                modifier = Modifier
//            )
//        }


        }
    }
}