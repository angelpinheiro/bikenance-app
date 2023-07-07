package com.anxops.bkn.ui.screens.garage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GarageBikeCard(
    bike: Bike,
    elevation: Dp = 5.dp,
    onEdit: () -> Unit = {},
    onDetail: () -> Unit = {}
) {


    val height = 110.dp

    val gradient = Brush.horizontalGradient(
        0f to MaterialTheme.colors.primary,
        0.1f to MaterialTheme.colors.primary,
        0.5f to MaterialTheme.colors.primary.copy(alpha = 0.9f),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 10.dp)
            .combinedClickable(
                onClick = { onDetail() },
                onLongClick = { onEdit() },
            ),

        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.primary
    ) {

        Box(
            modifier = Modifier
                .padding(0.dp)
                .clip(MaterialTheme.shapes.medium),
        ) {

            AsyncImage(
                url = bike.photoUrl,
                modifier = Modifier
                    .width(height + (height * 0.8f))
                    .height(height)
                    .padding(start = 0.dp, top = 1.dp, end = 1.dp, bottom = 1.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.CenterEnd),
                alignment = Alignment.TopCenter
            )
            Box(
                modifier =
                Modifier
                    .width(height + (height * 0.8f))
                    .height(height)
                    .align(Alignment.CenterEnd)
                    .background(gradient)
            )

            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, top = 16.dp, bottom = 10.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_bike,
                        MaterialTheme.colors.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        color = MaterialTheme.colors.onPrimary,
                        text = bike.displayName(),
                        style = MaterialTheme.typography.h3,
                    )
                }
                Text(
                    text = formatDistanceAsKm(bike.distance?.toInt() ?: 0),
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.secondary
                )


            }

        }
    }

}

@Composable
fun AsyncImage(
    url: String?,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
) {

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
            contentScale = contentScale,
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
            },
            alignment = alignment
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
                    .background(MaterialTheme.colors.surface),
                contentScale = ContentScale.Crop
            )
        }
    }

}

