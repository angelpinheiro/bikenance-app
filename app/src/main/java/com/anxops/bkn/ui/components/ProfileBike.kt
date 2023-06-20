package com.anxops.bkn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.shared.BknIcon
import com.anxops.bkn.ui.theme.BknGrey
import com.anxops.bkn.ui.theme.strava
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial


@Composable
fun ProfileBike(bike: Bike, onCheckedChange: ((Boolean) -> Unit) = {}) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 1.dp,
    ) {

        Column(
            modifier = Modifier
                .width(250.dp)
                .background(MaterialTheme.colors.surface)
                .padding(10.dp),
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BknIcon(
                    CommunityMaterial.Icon.cmd_bike,
                    BknGrey,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = (bike.name ?: ""),
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
            }

            Text(
                text = (bike.brandName ?: "") + " " + (bike.modelName ?: ""),
                style = MaterialTheme.typography.h3,
            )

            Text(
                text = (bike.distance ?: 0).div(1000f).toInt().toString() + " km",
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "Sync with strava", color = MaterialTheme.colors.strava)
                Switch(
                    checked = !bike.draft,
                    onCheckedChange = onCheckedChange,
                    colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.strava)
                )
            }

        }
    }

}