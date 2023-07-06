package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.ui.screens.garage.components.AsyncImage
import com.anxops.bkn.util.formatDistanceAsKm

@Composable
fun BikeDetailsHeader(bike: Bike) {

    val headerBg = MaterialTheme.colors.primaryVariant

    val gradient = Brush.horizontalGradient(
        0f to headerBg,
        1f to headerBg.copy(alpha = 0.7f),
    )

    val verticalGradient = Brush.verticalGradient(
        0f to headerBg,
        1f to headerBg.copy(alpha = 0.8f),
    )

    val headerHeight = 170.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(headerBg)
    ) {

        Column(Modifier.fillMaxHeight()) {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    url = bike.photoUrl,
                    modifier = Modifier
                        .fillMaxSize(),
                    alignment = Alignment.TopCenter
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(gradient)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(verticalGradient)
                )

            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter)
        ) {

            HeaderInfo(item = "Brand", detail = bike.brandName ?: "")
            HeaderInfo(item = "Model", detail = bike.modelName ?: "")
            HeaderInfo(item = "Distance", detail = formatDistanceAsKm(bike.distance?.toInt() ?: 0))

            Row {
                Text(
                    bike.type.extendedType,
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .background(
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
        }

        if (bike.stravaId != null) {

            Image(
                painter = painterResource(id = R.drawable.ic_strava_logo),
                contentDescription = null,

                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .padding(5.dp),
            )
        }


    }
}


@Composable
fun HeaderInfo(
    item: String,
    detail: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = modifier
                .weight(1f)
                .padding(start = 4.dp),
            text = "$item: ",
            color = color,
            style = MaterialTheme.typography.h3,

            )
        Text(
            modifier = modifier
                .weight(3f)
                .padding(start = 3.dp),
            text = detail,
            color = color.copy(alpha = 0.8f),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Bold,
        )
    }
}
