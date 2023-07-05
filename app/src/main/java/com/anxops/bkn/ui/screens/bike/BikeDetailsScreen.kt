package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponentType
import com.anxops.bkn.data.model.defaultComponentTypes
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.components.AsyncImage
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.bgGradient
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.text.DecimalFormat
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun BikeDetailsScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeDetailsScreenViewModel = hiltViewModel(),
    bikeId: String = ""
) {
    val bknNav = BknNavigator(navigator)
    val isRefreshing = remember { mutableStateOf(false) }
    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing.value, onRefresh = { })

    LaunchedEffect(bikeId) {
        viewModel.loadBike(bikeId)
    }

    val scrollState = rememberScrollState()

    val gradient = bgGradient()
    val state = viewModel.state.collectAsState()


    state.value.bike?.let { bike ->

        Scaffold(
            topBar = { BikeDetailsTopBar(bike = bike) }
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(bgGradient())
                    .pullRefresh(pullRefreshState)
            ) {
                Column(Modifier.verticalScroll(scrollState)) {

                    BikeDetailsHeader(bike = bike)

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgGradient())
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Components",
                            modifier = Modifier.padding(10.dp),
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onPrimary
                        )

                        BikeComponentType.values().forEach { type ->

                            val desc = defaultComponentTypes[type]?.name ?: "Unknown"

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                BikeComponentIcon(
                                    type = type,
                                    tint = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colors.primary)
                                        .padding(10.dp)
                                )

                                Text(
                                    text = desc,
                                    modifier = Modifier.padding(10.dp),
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.onPrimary
                                )
                            }

                        }

                    }
                }
            }
        }
    }
}

@Composable
fun BikeDetailsHeader(bike: Bike) {

    val gradient = Brush.horizontalGradient(
        0f to MaterialTheme.colors.primary,
        0.5f to MaterialTheme.colors.primary.copy(alpha = 0.95f),
    )

    val headerHeight = 150.dp


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
    ) {

        AsyncImage(
            url = bike.photoUrl,
            modifier = Modifier
                .width(headerHeight)
                .height(headerHeight)
                .padding(1.dp)
                .align(Alignment.CenterEnd)
        )
        Box(
            modifier =
            Modifier
                .width(headerHeight)
                .height(headerHeight)
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


@Composable
fun BikeDetailsTopBar(bike: Bike) =

    TopAppBar(
        elevation = 6.dp,
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {

                BknIcon(
                    icon = CommunityMaterial.Icon.cmd_bike,
                    color = Color.White,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(20.dp)
                )
                Text(
                    text = bike.displayName(),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {

                }) {
                    BknIcon(
                        icon = CommunityMaterial.Icon.cmd_attachment_plus,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }


