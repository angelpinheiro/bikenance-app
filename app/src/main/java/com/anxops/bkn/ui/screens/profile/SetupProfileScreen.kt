package com.anxops.bkn.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.profile.components.ProfileBike
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.onSurfaceTextFieldColors
import com.anxops.bkn.ui.theme.statusGood
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: SetupProfileScreenViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val profile = viewModel.profileState.collectAsState()
    val isNew = profile.value?.createdAt == null

    val nav = BknNavigator(navigator)


    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = (ActivityResultContracts.GetContent()),
        onResult = { uri ->
            imageData.value = uri
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                val imageByteArray = inputStream?.readBytes()
                viewModel.updateProfileImage(imageByteArray)
            }
        }
    )

    LaunchedEffect(key1 = context) {

        viewModel.updateEvent.collect { success ->
            if (success) {
                resultNavigator.navigateBack(true)
            }
        }
    }

    val colors = onSurfaceTextFieldColors()

    when (state.value.status) {
        SetupProfileScreenStatus.LoadingProfile -> {
            Loading()
        }

        SetupProfileScreenStatus.SavingProfile -> {
            Box(Modifier.fillMaxSize()) {
                Loading()
                Text(
                    text = "Updating profile...",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )
            }

        }

        else -> {


            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(20.dp), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {


                Text(
                    text = if (isNew) "Welcome to Bikenance!" else "Hi, ${profile.value?.firstname}!",
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(start = 10.dp),
                    color = MaterialTheme.colors.onSurface
                )

                Text(
                    text = if (isNew) "Let's take a minute to setup your profile!" else "You can edit your profile below",
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(start = 10.dp),
                    color = MaterialTheme.colors.onSurface
                )

                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    Box {

                        IconButton(onClick = {
                            launcher.launch("image/jpeg")
                        }) {
                            CircularProgressIndicator(
                                progress = state.value.profileImagePercent,
                                modifier = Modifier.size(110.dp),
                                strokeWidth = 5.dp,
                                color = MaterialTheme.colors.statusGood
                            )
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(profile.value?.profilePhotoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(0.dp)
                                    .clip(CircleShape),
                                loading = {
                                    CircularProgressIndicator(
                                        strokeWidth = 5.dp,
                                        color = MaterialTheme.colors.statusGood
                                    )
                                },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.padding(top = 26.dp)
                    )

                    BknLabelTopTextField(
                        value = profile.value?.firstname,
                        label = "First name",
                        colors = colors,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.updateFirstname(it)

                        })
                    BknLabelTopTextField(value = profile.value?.lastname,
                        label = "Last name",
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors,
                        onValueChange = {
                            viewModel.updateLastname(it)
                        })

                    val bikesTitle = if (isNew)
                        "We found ${state.value.bikes.size} bikes on Strava"
                    else
                        "Tracking ${state.value.bikes.filter { !it.draft }.size} bikes from Strava"

                    Text(
                        text = bikesTitle, modifier = Modifier
                            .padding(top = 26.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.h2
                    )
                    Text(
                        text = "Check which ones you want to import and synchronize to track their maintenance",
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.Start),
                        style = MaterialTheme.typography.h3
                    )
                }

                Column(
                    modifier =
                    Modifier
                        .padding(horizontal = 0.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    state.value.bikes.sortedByDescending { it.distance }
                        .forEach { bike ->
                            ProfileBike(bike = bike) {
                                viewModel.syncBike(bike._id)
                            }
                        }

                }

                Button(
                    onClick = { viewModel.saveProfileChanges() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    enabled = (profile.value?.firstname != null && profile.value?.lastname != null)
                ) {

                    Text(text = "Save changes", modifier = Modifier.padding(4.dp))
                }

            }
        }
    }

}