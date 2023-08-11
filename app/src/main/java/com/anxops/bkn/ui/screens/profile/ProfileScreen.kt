package com.anxops.bkn.ui.screens.profile

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.BknNavigator
import com.anxops.bkn.ui.screens.destinations.HomeScreenDestination
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.Message
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.CircularImagePicker
import com.anxops.bkn.ui.shared.components.ErrorDialog
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileScreenViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>
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
                Loading(stringResource(R.string.loading_profile))
            }

            ProfileScreenStatus.Saving -> {
                Loading(stringResource(R.string.updating_profile))
            }

            ProfileScreenStatus.UpdateSuccess -> {
                Message(
                    text = stringResource(R.string.done_message)
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
                            text = stringResource(R.string.profile_hi, state.profile.firstname ?: ""),
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier.padding(start = 10.dp),
                            color = MaterialTheme.colors.onPrimary
                        )

                        Text(
                            text = stringResource(R.string.edit_profile_subtitle),
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
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            CircularImagePicker(
                                url = state.profile.profilePhotoUrl,
                                defaultImageResId = R.drawable.default_bike_image,
                                onError = {
                                    viewModel.onUpdateProfileImageError()
                                }
                            ) { viewModel.onUpdateProfileImage(it) }
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            BknLabelTopTextField(
                                value = state.profile.firstname,
                                label = stringResource(R.string.profile_first_name_label),
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    viewModel.updateFirstname(it)
                                }
                            )
                            BknLabelTopTextField(
                                value = state.profile.lastname,
                                label = stringResource(R.string.profile_last_name_label),
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    viewModel.updateLastname(it)
                                }
                            )
                        }
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
                                text = stringResource(R.string.save_profile_button_text),
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
fun ProfileEditTopBar(
    onLogout: () -> Unit = {},
    onClickBack: () -> Unit = {}
) {
    TopAppBar(
        contentPadding = PaddingValues(5.dp),
        backgroundColor = MaterialTheme.colors.primaryVariant,
        elevation = 5.dp
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(modifier = Modifier.padding(start = 6.dp), onClick = { onClickBack() }) {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_arrow_left,
                            color = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
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
