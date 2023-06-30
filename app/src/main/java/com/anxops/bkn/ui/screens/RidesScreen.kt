package com.anxops.bkn.ui.screens

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
import com.anxops.bkn.model.Bike
import com.anxops.bkn.model.BikeRide
import com.anxops.bkn.ui.components.Ride
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.shared.BknIcon
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.coloredShadow
import com.anxops.bkn.util.formatAsYearMonth
import com.anxops.bkn.util.toDate
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.util.*

@Composable
fun RidesScreen(
    navigator: DestinationsNavigator,
    viewModel: RidesScreenViewModel = hiltViewModel(),
) {



    val state = viewModel.state.collectAsState()
    val bknNav = BknNavigator(navigator)

    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        viewModel.openActivityEvent.collect {
            openStravaActivity(context, it)
        }
    }

    val bikes = viewModel.bikes.collectAsState()
    val pagedRides = viewModel.getRidesFlow().collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
//        Row(modifier = Modifier.padding(10.dp)) {
//            Text(text = "", modifier = Modifier.padding(5.dp))
//            Button(onClick = { pagedRides.refresh() }) {
//                Text(
//                    text = "Refresh",
//                    style = MaterialTheme.typography.h5,
//                    color = MaterialTheme.colors.onPrimary
//                )
//            }
//        }
        Box(modifier = Modifier
            .fillMaxSize()) {
            PagedRideList(rides = pagedRides, bikes = bikes.value)
        }

    }


//    if (rides.value == null) {
//        Loading()
//    } else if (rides.value?.isEmpty() == true) {
//        EmptyRides()
//    } else {
//        RideList(rides = rides.value ?: emptyList(), bikes.value, onClickOpenStrava = {
//            viewModel.openActivity(it)
//        }, onClickRide = {
//            bknNav.navigateToRide(it)
//        })
//    }
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
    onClickOpenStrava: (stravaId: String) -> Unit = {}
) {


    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.background(MaterialTheme.colors.primary)
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