package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.garage.components.AsyncImage
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.componentResourcesMap
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.bgGradient
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.util.formatDistanceAsKm
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
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

    val ct = remember { ComponentTypes.values() }

    val lazyColumnState = rememberLazyListState()

    state.value.bike?.let { bike ->

        Scaffold(
            topBar = { BikeDetailsTopBar(bike = bike) },

            ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryVariant)
                    .pullRefresh(pullRefreshState)
            ) {
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier
                        .fillMaxSize()
//                        .verticalScroll(scrollState)
                        .background(MaterialTheme.colors.primaryVariant)
                ) {

                    item {
                        BikeDetailsHeader(bike = bike)
                    }
                    stickyHeader {
                        Text(
                            text = "Components",
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colors.primary)
                                .padding(10.dp),
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.onPrimary
                        )

                    }

                    items(count = ct.size) { index ->

                        val type = ct[index]
                        val resources = type.resources()

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.surface)
                        ) {

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

                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(resources.nameResId),
                                    modifier = Modifier.padding(0.dp),
                                    style = MaterialTheme.typography.h3,
                                    color = MaterialTheme.colors.primary
                                )
                                Text(
                                    text = stringResource(resources.descriptionResId),
                                    modifier = Modifier.padding(0.dp),
                                    style = MaterialTheme.typography.h4,
                                    color = MaterialTheme.colors.primary
                                )

                            }


                        }

                    }

//
//                        defaultComponentTypes.forEach { (type, value) ->
//
//
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//
//                                BikeComponentIcon(
//                                    type = type,
//                                    tint = MaterialTheme.colors.onPrimary,
//                                    modifier = Modifier
//                                        .padding(10.dp)
//                                        .size(60.dp)
//                                        .clip(CircleShape)
//                                        .background(MaterialTheme.colors.primary)
//                                        .padding(10.dp)
//                                )
//
//                                Column(
//                                    modifier = Modifier.padding(10.dp),
//                                    verticalArrangement = Arrangement.Center
//                                ) {
//                                    Text(
//                                        text = value.name,
//                                        modifier = Modifier.padding(0.dp),
//                                        style = MaterialTheme.typography.h3,
//                                        color = MaterialTheme.colors.primary
//                                    )
//                                    Text(
//                                        text = value.description,
//                                        modifier = Modifier.padding(0.dp),
//                                        style = MaterialTheme.typography.h4,
//                                        color = MaterialTheme.colors.primary
//                                    )
//
//                                }
//
//
//                            }

                }
            }
        }
    }
}


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


@Composable
fun BikeDetailsTopBar(bike: Bike) =

    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
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

//                AsyncImage(
//                    url = bike.photoUrl,
//                    modifier = Modifier
//                        .padding(6.dp)
//                        .size(40.dp)
//                        .clip(CircleShape)
//                )

                Text(
                    text = bike.name ?: bike.displayName(),
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


