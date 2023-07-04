/*
 * Copyright 2023 Angel Piñeiro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anxops.bkn.ui.screens.maintenances.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusOk
import com.anxops.bkn.ui.theme.statusWarning
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial


data class MaintenanceItem(
    val bike: String,
    val bikePart: String,
    val title: String,
    val time: String,
    val percentage: Float
)

@Composable
fun MaintenanceItemView(item: MaintenanceItem) {

    var open by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 5.dp)
            .background(MaterialTheme.colors.surface)
            .clickable {
                open = !open
            }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = item.percentage,
                color = getColorForProgress(percentage = item.percentage).copy(alpha = 0.9f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    BknIcon(
                        CommunityMaterial.Icon3.cmd_tools,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(16.dp)
                    )
                    Text(
                        item.bikePart,
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }

        }

        Row (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    item.title,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background, shape = CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
                Text(
                    item.time,
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(horizontal = 10.dp).padding(top = 6.dp)
                )
            }

            Button(onClick = {}, modifier = Modifier.padding(horizontal = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_check,
                        MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }

        if (open) {
            Row(
                horizontalArrangement = Arrangement.End, modifier = Modifier
                    .fillMaxWidth()
            ) {

                OutlinedButton(onClick = {}) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_bike,
                            MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(16.dp)
                        )
                        Text(
                            text = "Delay",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
                Button(onClick = {}, modifier = Modifier.padding(horizontal = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_check,
                            MaterialTheme.colors.onSecondary,
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(16.dp)
                        )
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onSecondary
                        )
                    }
                }


            }
        }
    }

}

@Composable
fun getColorForProgress(percentage: Float): Color {
    return when {
        percentage >= 1 -> {
            MaterialTheme.colors.statusDanger
        }

        percentage > 0.8 -> {
            MaterialTheme.colors.statusWarning
        }

        percentage > 0.5 -> {
            MaterialTheme.colors.statusOk
        }

        else -> {
            MaterialTheme.colors.statusGood
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MaintenanceItemPreview() {
    BikenanceAndroidTheme {
        MaintenanceItemView(FakeData.maintenances.first())
    }
}

@Preview(showBackground = false)
@Composable
fun MaintenanceItemPreview2() {
    BikenanceAndroidTheme {
        MaintenanceItemView(FakeData.maintenances[2])
    }
}
