package com.anxops.bkn.ui.screens.bike


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.bike.components.BikeDetailsHeader
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.bgGradient
import com.anxops.bkn.ui.shared.resources
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
    val checked = remember {
        mutableStateOf(setOf<ComponentTypes>())
    }

    state.value.bike?.let { bike ->

        Scaffold(
            topBar = { BikeDetailsTopBar(bike = bike) },

            ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primaryVariant)
                    .pullRefresh(pullRefreshState)
            ) {


                ComponentList(
                    checkable = true,
                    headerContent = {
                        BikeDetailsHeader(bike = bike)
                    }, onCheckChanged = { checkedItems ->
                        checked.value = checkedItems
                    }
                )

                Text(text = checked.value.joinToString { it.name })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComponentList(
    headerContent: @Composable () -> Unit = {}, checkable: Boolean = true,
    onCheckChanged: (Set<ComponentTypes>) -> Unit = {}
) {

    val componentTypes = remember { ComponentTypes.values() }
    val lazyColumnState = rememberLazyListState()
    val checkedItems = remember { mutableStateOf(setOf<ComponentTypes>()) }

    LazyColumn(
        state = lazyColumnState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primaryVariant)
    ) {

        item {
            headerContent()
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

        items(count = componentTypes.size) { index ->
            val type = componentTypes[index]
            val checked = remember {
                checkedItems.value.contains(type)
            }
            ComponentListItem(
                type = type,
                checkable = checkable,
                checked = checked,
                onCheckChanged = { isChecked ->
                    if (isChecked)
                        checkedItems.value = checkedItems.value.plus(type)
                    else
                        checkedItems.value = checkedItems.value.minus(type)
                })

        }
    }
}


@Composable
fun ComponentListItem(
    type: ComponentTypes,
    checkable: Boolean = false,
    checked: Boolean = true,
    onCheckChanged: (Boolean) -> Unit = {}
) {

    val resources = type.resources()

    val isChecked = remember {
        mutableStateOf(checked)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(end = 10.dp)
            .padding(vertical = 5.dp)
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
            modifier = Modifier
                .padding(10.dp)
                .weight(1f), verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(resources.nameResId),
                modifier = Modifier.padding(0.dp),
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = stringResource(resources.descriptionResId),
                modifier = Modifier
                    .padding(0.dp),
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.primary
            )

        }
        if (checkable) {
            RadioButton(selected = isChecked.value, onClick = {
                isChecked.value = isChecked.value.not()
                onCheckChanged(isChecked.value)
            })
        }
    }
}

@Composable
fun BikeDetailsTopBar(bike: Bike) =

    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 0.dp
    ) {
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


