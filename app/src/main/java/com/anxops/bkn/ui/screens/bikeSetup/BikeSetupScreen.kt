package com.anxops.bkn.ui.screens.bikeSetup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.maintenanceConfigurations
import com.anxops.bkn.ui.shared.components.bgGradient
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
@Destination
fun BikeSetupScreen(
    navigator: DestinationsNavigator,
    viewModel: BikeSetupViewModel = hiltViewModel(),
    bikeId: String = ""
) {

    LaunchedEffect(bikeId) {
        viewModel.loadBike(bikeId)
    }

    val pageCount = 3
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()

    val data = viewModel.setupData.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary)
            .background(bgGradient())
            .padding(10.dp),
    ) {

        Box(modifier = Modifier.weight(1f)) {
            HorizontalPager(
                pageCount = pageCount,
                state = pagerState,
            ) { page ->

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Text(text = "$page/$pageCount")

                    Box {
                        when (pagerState.currentPage) {
                            0 -> {
                                FirstPage(data = data.value, onBikeTypeSelected = {
                                    viewModel.onBikeTypeSelected(it)
                                })
                            }

                            1 -> {
                                SecondPage()
                            }

                            2 -> {
                                ThirdPage()
                            }
                        }
                    }
                }


            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                scope.launch {
                    if (pagerState.currentPage > 0) {
                        pagerState.scrollToPage(pagerState.currentPage - 1)
                    }
                }
            }, Modifier.padding(horizontal = 3.dp)) {
                Text(text = "Prev")
            }
            Button(onClick = {
                scope.launch {
                    pagerState.scrollToPage((pagerState.currentPage + 1) % pageCount)
                }
            }, Modifier.padding(horizontal = 3.dp)) {
                Text(text = "Next")
            }
        }
    }


}

@Composable
fun BikeSetupTitle(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 20.dp),
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h1
    )
}


@Composable
fun BikeSetupDescription(text: String) {
    Text(
        modifier = Modifier,
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h2,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BikeSetupText(text: String) {
    Text(
        modifier = Modifier,
        text = text,
        color = MaterialTheme.colors.onPrimary,
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Normal
    )
}

@Composable
fun BikeSetupDivider(size: Dp) {
    Box(Modifier.height(size))
}


@Composable
fun FirstPage(data: BikeSetupData, onBikeTypeSelected: (bikeType: BikeType) -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BikeSetupTitle(text = "Bike type")
        BikeSetupDescription(text = "To provide you with accurate maintenance tracking, we need a few details about your bike and riding habits")
        BikeSetupDivider(30.dp)
        BikeSetupDescription(text = "Let's start with your bike type")

        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            maintenanceConfigurations.forEach {
                OutlinedButton(
                    onClick = {
                        onBikeTypeSelected(it.key)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(0.5f),
                    colors = if (data.bikeType == it.key) {
                        ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.secondary,
                        )

                    } else {
                        ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                        )
                    }
                ) {
                    Text(
                        text = it.key.type,
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h5,
                    )
                }
            }
        }

    }
}



@Composable
fun SecondPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BikeSetupTitle(text = "Weekly Mileage")
        BikeSetupDescription(text = "Approximately, how many kilometers do you ride per week?")
    }
}

@Composable
fun ThirdPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        BikeSetupTitle(text = "Maintenance status")
        BikeSetupDescription(text = "When was the last time you had a complete maintenance for your bike?")
    }
}

