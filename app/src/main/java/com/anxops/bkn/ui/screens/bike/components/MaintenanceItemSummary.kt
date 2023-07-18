package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatAsDayMonth
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun MaintenanceItemSummary(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
//

//
//        Text(
//            text = stringResource(id = item.type.resources().descriptionResId),
//            color = MaterialTheme.colors.secondary,
//            style = MaterialTheme.typography.h5,
//            modifier = Modifier.padding(bottom = 5.dp)
//        )
//
//        MaintenanceDetailEntry(
//            "Last maintenance", "${item.lastMaintenanceDate?.formatAsDayMonth()}"
//        )
//        MaintenanceDetailEntry(
//            "Next maintenance", "${item.estimatedDate?.formatAsDayMonth()}"
//        )
//        MaintenanceDetailEntry(
//            "Usage", "${(item.status * 10000).toInt() / 100}%"
//        )
//        MaintenanceDetailEntry(
//            "Recommended maintenance",
//            "Every ${item.defaultFrequency.every} ${item.defaultFrequency.unit.name.lowercase()}"
//        )

        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically

        ) {

//            BknIcon(
//                icon = CommunityMaterial.Icon3.cmd_minus_circle,
//                color = getColorForStatus(StatusLevel.from(item.status)),
//                modifier = Modifier
//                    .size(16.dp)
//            )

            CircularProgressIndicator(
                progress = item.status.toFloat(),
                color = getColorForStatus(StatusLevel.from(item.status)),
                modifier = Modifier.padding(end = 10.dp).size(22.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant,
                strokeWidth = 6.dp
            )

            Text(
                text = "Estimated maintenance ${item.estimatedDate?.formatAsDayMonth()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .weight(1f)
            )
//
//            LinearProgressIndicator(
//                progress = item.status.toFloat(),
//                modifier = Modifier
//                    .padding(vertical = 6.dp)
//                    .height(5.dp)
//                    .clip(RoundedCornerShape(20.dp))
//                    .weight(1.0f),
//                color = getColorForStatus(StatusLevel.from(item.status)),
//                backgroundColor = MaterialTheme.colors.primaryVariant,
//            )
        }
    }
}

@Composable
fun MaintenanceDetailEntry(key: String, value: String) {
    Text(
        text = "$key: $value",
        color = MaterialTheme.colors.background,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(start = 0.dp)
    )
}