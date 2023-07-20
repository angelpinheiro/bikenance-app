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
import com.anxops.bkn.util.formatAsRelativeTime
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun MaintenanceItemSummary(item: Maintenance) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {


        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp), verticalAlignment = Alignment.CenterVertically

        ) {

            CircularProgressIndicator(
                progress = item.status.toFloat(),
                color = getColorForStatus(StatusLevel.from(item.status)),
                modifier = Modifier.padding(end = 10.dp).size(22.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant,
                strokeWidth = 6.dp
            )

            Text(
                text = "Estimated maintenance ${item.estimatedDate?.formatAsRelativeTime()}",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h3,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}