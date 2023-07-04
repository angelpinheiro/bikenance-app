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

package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.mock.FakeData
import com.anxops.bkn.ui.screens.maintenances.components.getColorForProgress
import com.anxops.bkn.ui.theme.statusDanger

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OngoingMaintenance(bikes: List<Bike>, pagerState: PagerState) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {

        FakeData.maintenances
            .filter { it.bike == bikes[pagerState.currentPage].name }
            .forEach {

                Column(
                    modifier = Modifier
                        .padding(bottom = 1.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                        .padding(bottom = 20.dp),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        CircularProgressIndicator(
                            progress = it.percentage,
                            modifier = Modifier.size(26.dp),
                            strokeWidth = 6.dp,
                            color = getColorForProgress(percentage = it.percentage),
                            backgroundColor = MaterialTheme.colors.primary
                        )
                        Text(
                            text = it.bikePart,
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(horizontal = 10.dp)
//                            .weight(1f)
                        )
                        Divider(
                            color = getColorForProgress(percentage = it.percentage),
                            modifier = Modifier
                                .height(1.dp)
                                .padding(end = 10.dp)
                                .weight(1f)
                        )
                        Text(
                            text = it.title,
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primaryVariant, shape = CircleShape
                                )
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        )

                    }
                    Text(
                        text = "Last ${it.title}: 3 months ago",
                        color = MaterialTheme.colors.background,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(start = 36.dp)
                    )
                    if (it.percentage < 1.0) {
                        Text(
                            text = "Estimated duration: 2 months",
                            color = MaterialTheme.colors.background,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(start = 36.dp)
                        )
                    } else {
                        Text(
                            text = "Service required",
                            color = MaterialTheme.colors.onPrimary,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .padding(start = 34.dp)
                                .background(
                                    color = MaterialTheme.colors.statusDanger, shape = CircleShape
                                )
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        )
                    }

                    Divider(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(1.dp)
                    )

                }
            }
    }
}