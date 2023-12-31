package com.anxops.bkn.ui.screens.rides.list

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.rides.list.components.BikeRideItem
import com.anxops.bkn.ui.screens.rides.list.components.RideAndBike
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsRelativeTime
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.Instant
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RidesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel()
) {
    val bknNav = BknNavigator(navigator)

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.openActivityEvent.collect {
            openStravaActivity(context, it)
        }
    }

    val bikes = viewModel.bikes.collectAsState()
    val pagedRides = viewModel.paginatedRidesFlow.collectAsLazyPagingItems()
    val lastUpdated = viewModel.lastUpdatedFlow.collectAsState(null)

    val at = lastUpdated.value?.let {
        Instant.ofEpochMilli(it.lastRidesUpdate).formatAsRelativeTime()
    } ?: stringResource(R.string.never_updated)

    val isRefreshing = pagedRides.loadState.refresh == LoadState.Loading || pagedRides.loadState.append == LoadState.Loading

    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { pagedRides.refresh() })

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PagedRideList(rides = pagedRides, bikes = bikes.value, pullRefreshState = pullRefreshState, onClickOpenStrava = {
            viewModel.openActivity(it)
        }, onClickRide = {
            bknNav.navigateToRide(it)
        }, onBikeConfirm = { bike, ride ->
            viewModel.onBikeRideConfirmed(ride, bike)
        })

        Text(
            text = stringResource(R.string.rides_last_updated_text, at),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(10.dp).clip(RoundedCornerShape(5.dp)).background(MaterialTheme.colors.primaryVariant).padding(10.dp)
                .align(Alignment.BottomCenter)
        )
//        EmptyRides()
        PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))

        Divider(
            Modifier.fillMaxWidth().height(20.dp).background(rideListBottomShadow()).align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun rideListBottomShadow(): Brush {
    val color1 = Color.Transparent
    val color2 = MaterialTheme.colors.primaryVariant

    val gradient = remember {
        Brush.verticalGradient(
            0f to color1,
            1f to color2.copy(alpha = 0.3f)
        )
    }

    return gradient
}

@Composable
fun EmptyRides(onClickNew: () -> Unit = {}) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "You have no rides yet",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(bottom = 100.dp).align(Alignment.Center)
        )
        Button(
            onClick = onClickNew,
            modifier = Modifier.padding(horizontal = 10.dp).align(Alignment.Center)
        ) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_plus,
                modifier = Modifier.padding(end = 10.dp).size(20.dp),
                color = MaterialTheme.colors.onPrimary
            )
            Text(text = "Add a new ride")
        }
        Image(
            painter = painterResource(id = R.drawable.ic_undraw_not_found),
            contentDescription = "Not found",
            modifier = Modifier.fillMaxWidth(0.8f).padding(bottom = 30.dp).align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagedRideList(
    rides: LazyPagingItems<RideAndBike>,
    bikes: List<Bike>,
    onClickRide: (id: String) -> Unit,
    onClickOpenStrava: (stravaId: String) -> Unit,
    onBikeConfirm: (Bike, BikeRide) -> Unit,
    pullRefreshState: PullRefreshState
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState),
        contentPadding = PaddingValues(vertical = 10.dp)
    ) {
        items(count = rides.itemCount) { index ->
            rides[index]?.let { rideAndBike ->
                BikeRideItem(item = rideAndBike, bikes = bikes, onClick = {
                    onClickRide(rideAndBike.ride._id)
                }, onBikeConfirm = { bike, ride ->
                    onBikeConfirm(bike, ride)
                }, onClickOpenOnStrava = {
                    rideAndBike.ride.stravaId?.let { onClickOpenStrava(it) }
                })
            }
        }
    }
}

fun openStravaActivity(context: Context, activityId: String) {
    val url = "https://www.strava.com/activities/$activityId"
    val intent = CustomTabsIntent.Builder().build()
    intent.launchUrl(context, Uri.parse(url))
}
