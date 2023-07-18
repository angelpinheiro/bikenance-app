package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.util.formatAsDayMonth

@Composable
fun MaintenanceItemSummary(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        MaintenanceDetailEntry(
            "Last maintenance",
            "${item.lastMaintenanceDate?.formatAsDayMonth()}"
        )
        MaintenanceDetailEntry("Next maintenance", "${item.estimatedDate?.formatAsDayMonth()}")
        MaintenanceDetailEntry("Usage", "${(item.status * 10000).toInt() / 100}%")
        MaintenanceDetailEntry(
            "Recommended maintenance",
            "Every ${item.defaultFrequency.every} ${item.defaultFrequency.unit.name.lowercase()}"
        )
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