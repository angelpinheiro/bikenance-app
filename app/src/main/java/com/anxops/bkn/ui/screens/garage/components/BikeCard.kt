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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.R
import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.Loading
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.text.DecimalFormat


@Composable
fun BikeCardV2(bike: Bike, elevation: Dp = 5.dp, onEdit: () -> Unit = {}) {

    val configuration = LocalConfiguration.current

    val gradient = Brush.horizontalGradient(
        0f to MaterialTheme.colors.primary,
        0.5f to MaterialTheme.colors.primary.copy(alpha = 0.9f),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 10.dp)
            .clickable { onEdit() },
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {

        Box(
            modifier = Modifier.padding(0.dp).background(MaterialTheme.colors.primary)
        ) {

            AsyncImage(
                url = bike.photoUrl,
                modifier = Modifier
                    .width(180.dp)
                    .height(100.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .align(Alignment.CenterEnd)
            )
            Box(
                modifier =
                Modifier
                    .width(180.dp)
                    .height(100.dp)
                    .align(Alignment.CenterEnd)
                    .background(gradient)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {



                Column(Modifier.padding(15.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BknIcon(
                            CommunityMaterial.Icon.cmd_bike,
                            MaterialTheme.colors.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            color = MaterialTheme.colors.onPrimary,
                            text = bike.displayName(),
                            style = MaterialTheme.typography.h3,
                        )
                    }
                    Text(
                        text = DecimalFormat("###,###,###,###").format(
                            (bike.distance ?: 0).div(1000f)
                        ) + " km",
                        style = MaterialTheme.typography.h1,
                        color = MaterialTheme.colors.secondary
                    )

                }
            }

        }
    }

}

@Composable
fun AsyncImage(url: String?, modifier: Modifier = Modifier.fillMaxSize()) {

    val defaultImage = R.drawable.default_bike_image

    var imageLoadFinished by remember {
        mutableStateOf(false)
    }

    var imageBroken by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            loading = {
                Loading()
            },
            contentScale = ContentScale.Crop,
            onSuccess = {
                imageLoadFinished = true
            },
            onError = {
                imageLoadFinished = true
                imageBroken = true
            },
            onLoading = {
                imageLoadFinished = false
                imageBroken = false
            }
        )

        if (imageBroken) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(defaultImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.surface),
                contentScale = ContentScale.Crop
            )
        }
    }

}

