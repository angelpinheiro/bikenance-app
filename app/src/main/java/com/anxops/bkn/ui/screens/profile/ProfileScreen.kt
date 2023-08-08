package com.anxops.bkn.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.Message
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.ErrorDialog
import com.anxops.bkn.ui.theme.statusGood
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
) {

    val bknNavigator = BknNavigator(navigator)
    val state by viewModel.state.collectAsState()
    val event by viewModel.events.collectAsState()
    val logout by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.status) {
        if (state.status == ProfileScreenStatus.UpdateSuccess) {
            resultNavigator.navigateBack(true)
        }
    }

    LaunchedEffect(event) {
        when (event) {
            is ProfileScreenEvent.Logout -> {
                bknNavigator.popBackStackTo(HomeScreenDestination.route, true)
                bknNavigator.navigateToSplash()
            }

            else -> {}
        }

    }

    LaunchedEffect(true) {
        viewModel.loadProfile()
    }

    BackgroundBox(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state.status) {

            ProfileScreenStatus.Loading -> {
                Loading("Loading profile...")
            }

            ProfileScreenStatus.Saving -> {
                Loading("Updating profile...")
            }

            ProfileScreenStatus.UpdateSuccess -> {
                Message(
                    text = "Done!"
                )
            }

            ProfileScreenStatus.Loaded -> {

                Column(Modifier.fillMaxSize()) {

                    ProfileEditTopBar(onClickBack = {
                        bknNavigator.popBackStack()
                    }, onLogout = {
                        viewModel.onLogoutRequest()
                    })


                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Spacer(modifier = Modifier.height(26.dp))

                        Text(
                            text = "Hi, ${state.profile?.firstname}!",
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier.padding(start = 10.dp),
                            color = MaterialTheme.colors.onPrimary
                        )

                        Text(
                            text = "You can edit your profile below",
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.padding(start = 10.dp),
                            color = MaterialTheme.colors.onPrimary
                        )

                        Row(
                            modifier = Modifier
                                .padding(20.dp)
                                .padding(top = 20.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,

                            ) {


                            ProfileImageLoader(state.profile.profilePhotoUrl, onNewImageSelected = {
                                viewModel.onUpdateProfileImage(it)
                            })
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {

                            BknLabelTopTextField(value = state.profile.firstname,
                                label = "First name",
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    viewModel.updateFirstname(it)

                                })
                            BknLabelTopTextField(value = state.profile.lastname,
                                label = "Last name",
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    viewModel.updateLastname(it)
                                })

                        }

//                        Button(
//                            onClick = { viewModel.saveProfileChanges() },
//                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
//                            modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
//                            enabled = (state.profile.firstname != null && state.profile.lastname != null)
//                        ) {
//
//                            Text(
//                                text = "Save changes",
//                                modifier = Modifier.padding(4.dp),
//                                color = MaterialTheme.colors.surface,
//                            )
//                        }

                    }

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.primaryVariant)
                            .padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.saveProfileChanges() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                            modifier = Modifier.fillMaxWidth(),
                            enabled = (state.profile.firstname != null && state.profile.lastname != null)
                        ) {
                            Text(
                                text = "Save profile",
                                modifier = Modifier.padding(4.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }

                }
            }
        }

        state.error?.let {
            ErrorDialog(appError = it, bknNavigator = bknNavigator, onDismissRequest = {
                viewModel.onDismissError()
            })
        }
    }

}

@Composable
private fun ProfileImageLoader(
    url: String?,
    onNewImageSelected: (ByteArray) -> Unit = {},
    onError: () -> Unit = {},
) {

    val progress = remember {
        mutableStateOf(0f)
    }

    val context = LocalContext.current
    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageData.value = uri
            uri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.readBytes()?.let { onNewImageSelected(it) }
            }
        })

    Box {

        IconButton(onClick = {
            launcher.launch("image/jpeg")
        }) {
            CircularProgressIndicator(
                progress = progress.value,
                modifier = Modifier.size(110.dp),
                strokeWidth = 5.dp,
                color = MaterialTheme.colors.statusGood,
            )
            SubcomposeAsyncImage(model = url?.let {
                ImageRequest.Builder(LocalContext.current).data(url).crossfade(true).build()
            },
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(0.dp)
                    .clip(CircleShape),
                loading = {
                    CircularProgressIndicator(
                        strokeWidth = 5.dp, color = MaterialTheme.colors.statusGood
                    )
                },
                contentScale = ContentScale.Crop,
                onLoading = {

                },
                onError = {
                    onError()
                })
        }
    }
}


@Composable
fun ProfileEditTopBar(
    onLogout: () -> Unit = {}, onClickBack: () -> Unit = {}
) {

    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 5.dp,
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(modifier = Modifier.padding(start = 6.dp),
                        onClick = { onClickBack() }) {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_arrow_left,
                            color = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
//
//                    Text(
//                        text = bike.name ?: bike.displayName(),
//                        color = MaterialTheme.colors.onPrimary,
//                        style = MaterialTheme.typography.h2,
//                        modifier = Modifier.padding(start = 12.dp, end = 4.dp)
//                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        onLogout()
                    }) {
                        Text("Logout", modifier = Modifier.padding(end = 10.dp))
                        BknIcon(
                            CommunityMaterial.Icon.cmd_exit_to_app,
                            MaterialTheme.colors.surface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}