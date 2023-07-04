package com.anxops.bkn.ui.screens.rides.list

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeRide
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.rides.list.components.Ride
import com.anxops.bkn.ui.shared.coloredShadow
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.util.formatAsYearMonth
import com.anxops.bkn.util.simpleLocalTimeFormat
import com.anxops.bkn.util.toDate
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RidesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel(),
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

    val at =
        lastUpdated.value?.let { simpleLocalTimeFormat.format(Date(it.lastRidesUpdate)) } ?: "Never"

    val isRefreshing =
        pagedRides.loadState.refresh == LoadState.Loading || pagedRides.loadState.append == LoadState.Loading

    val pullRefreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { pagedRides.refresh() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        PagedRideList(rides = pagedRides, bikes = bikes.value,
            modifier = Modifier.padding(top = 30.dp),
            onClickOpenStrava = {
                viewModel.openActivity(it)
            }, onClickRide = {
                bknNav.navigateToRide(it)
            })
        Text(
            text = "Last Updated: $at",
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primaryVariant)
                .padding(6.dp)
        )
        // EmptyRides()
        PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}


@Composable
fun EmptyRides(onClickNew: () -> Unit = {}) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = "You have no rides yet",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(bottom = 100.dp)
                .align(Alignment.Center)
        )
        Button(
            onClick = onClickNew,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .align(Alignment.Center)
        ) {
            BknIcon(
                icon = CommunityMaterial.Icon3.cmd_plus,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(20.dp),
                color = MaterialTheme.colors.onPrimary
            )
            Text(text = "Add a new ride")
        }
        Image(
            painter = painterResource(id = R.drawable.ic_undraw_not_found),
            contentDescription = "Not found",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 30.dp)
                .align(Alignment.BottomCenter)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagedRideList(
    rides: LazyPagingItems<BikeRide>,
    bikes: List<Bike>,
    onClickRide: (id: String) -> Unit = {},
    onClickOpenStrava: (stravaId: String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = modifier.background(MaterialTheme.colors.primary)
    ) {

        items(count = rides.itemCount) { index ->
            rides[index]?.let {
                Ride(ride = it, bikes, onClickOpenOnStrava = {
                    it.stravaId?.let { id ->
                        onClickOpenStrava(id)
                    }
                }, onClick = {
                    onClickRide(it._id)
                })

            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RideList(
    rides: List<BikeRide>,
    bikes: List<Bike>,
    onClickRide: (id: String) -> Unit = {},
    onClickOpenStrava: (stravaId: String) -> Unit = {}
) {

    val loadedRides = rides.groupBy { it.dateTime?.substring(0, 7) ?: "" }?.toList()
        ?.sortedByDescending { it.first }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.background(MaterialTheme.colors.primary)
    ) {

        loadedRides?.forEach { item ->

            val gRides = item.second.sortedByDescending { it.dateTime }
            val date = gRides.first().dateTime.toDate()?.formatAsYearMonth() ?: "Other"

            stickyHeader {
                Text(
                    text = date.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .coloredShadow()
                        .background(MaterialTheme.colors.primaryVariant)
                        .padding(6.dp),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h3
                )
            }
            items(items = gRides, itemContent = {
                Ride(ride = it, bikes, onClickOpenOnStrava = {
                    it.stravaId?.let { id ->
                        onClickOpenStrava(id)
                    }
                }, onClick = {
                    onClickRide(it._id)
                })
            })
        }
    }
}


fun openStravaActivity(context: Context, activityId: String) {
    val url = "https://www.strava.com/activities/$activityId"
    val intent = CustomTabsIntent
        .Builder()
        .build()
    intent.launchUrl(context, Uri.parse(url))
}