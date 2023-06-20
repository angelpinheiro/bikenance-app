package com.anxops.bkn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.R
import com.anxops.bkn.model.Bike
import com.anxops.bkn.ui.shared.BknIcon
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.theme.strava
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import java.text.DecimalFormat


@Composable
fun Bike(bike: Bike, onEdit: () -> Unit = {}) {

    var imageLoadFinished by remember {
        mutableStateOf(false)
    }

    val defaultImage = R.drawable.default_bike_image

    var imageBroken by remember {
        mutableStateOf(false)
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.surface)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Divider(Modifier.height(40.dp), color = Color.Transparent)
            IconButton(onClick = onEdit) {
                Box(
                    Modifier
                        .size(320.dp)
                        .clip(CircleShape)
                ) {

                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(bike.photoUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.surface),
                            loading = {
                                Loading()
                            },
                            contentScale = ContentScale.Fit,
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
                            contentScale = ContentScale.Fit
                        )
                    }
                    if (imageLoadFinished) {
                        Box(
                            Modifier
                                .fillMaxSize()
                                .padding(0.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        0.0f to Color.Transparent,
                                        0.85f to Color.Transparent,
                                        0.9f to MaterialTheme.colors.primary.copy(alpha = 0.001f),
                                        0.95f to MaterialTheme.colors.primary.copy(alpha = 0.02f),
                                        1.0f to MaterialTheme.colors.primary.copy(alpha = 0.03f)
                                    ),
                                )
                        ) {
                            if (imageBroken && bike.photoUrl != null) {
                                BknIcon(
                                    CommunityMaterial.Icon2.cmd_image_broken_variant,
                                    MaterialTheme.colors.primary.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .padding(bottom = 80.dp)
                                        .size(36.dp)
                                        .align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BknIcon(
                    CommunityMaterial.Icon.cmd_bike,
                    MaterialTheme.colors.strava,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = bike.name ?: "",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            Text(
                text = (bike.brandName ?: "") + " " + (bike.modelName ?: ""),
                style = MaterialTheme.typography.h3,
            )
            Text(
                text = DecimalFormat("###,###,###,###").format(
                    (bike.distance ?: 0).div(1000f)
                ) + " km",
                style = MaterialTheme.typography.h2
            )

        }
        if(bike.stravaId != null) {
            Image(
                painter = painterResource(R.drawable.ic_strava_logo),
                contentDescription = "",
                modifier = Modifier
                    .padding(16.dp)
                    .width(20.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }

}