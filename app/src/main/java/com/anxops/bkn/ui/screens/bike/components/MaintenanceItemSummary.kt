package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatAsDayMonth

@Composable
fun MaintenanceItemSummary(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(id = item.type.resources().nameResId),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(top = 10.dp)
        )

        Text(
            text = stringResource(id = item.type.resources().descriptionResId),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        MaintenanceDetailEntry(
            "Last maintenance", "${item.lastMaintenanceDate?.formatAsDayMonth()}"
        )
        MaintenanceDetailEntry(
            "Next maintenance", "${item.estimatedDate?.formatAsDayMonth()}"
        )
        MaintenanceDetailEntry(
            "Usage", "${(item.status * 10000).toInt() / 100}%"
        )
        MaintenanceDetailEntry(
            "Recommended maintenance",
            "Every ${item.defaultFrequency.every} ${item.defaultFrequency.unit.name.lowercase()}"
        )

        Row(
            Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically

        ) {

            LinearProgressIndicator(
                progress = item.status.toFloat(),
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1.0f),
                color = getColorForStatus(StatusLevel.from(item.status)),
                backgroundColor = MaterialTheme.colors.primaryVariant,
            )
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