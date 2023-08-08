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
import androidx.compose.material.TextFieldDefaults
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
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.Message
import com.anxops.bkn.ui.shared.components.BackgroundBox
import com.anxops.bkn.ui.shared.components.BknLabelTopTextField
import com.anxops.bkn.ui.shared.components.ErrorDialog
import com.anxops.bkn.ui.theme.statusGood
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

    LaunchedEffect(state.status) {
        if (state.status == ProfileScreenStatus.UpdateSuccess) {
            resultNavigator.navigateBack(true)
        }
    }

    LaunchedEffect(true) {
        viewModel.loadProfile()
    }

    val colors = TextFieldDefaults.textFieldColors(
        backgroundColor = MaterialTheme.colors.surface,
        cursorColor = MaterialTheme.colors.onSurface,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        textColor = MaterialTheme.colors.onSurface,
    )


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


                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {

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
                        Text(
                            text = "Profile",
                            style = MaterialTheme.typography.h2,
                            color = MaterialTheme.colors.surface,
                            modifier = Modifier.padding(top = 26.dp)
                        )

                        BknLabelTopTextField(value = state.profile.firstname,
                            label = "First name",
                            colors = colors,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                viewModel.updateFirstname(it)

                            })
                        BknLabelTopTextField(value = state.profile.lastname,
                            label = "Last name",
                            modifier = Modifier.fillMaxWidth(),
                            colors = colors,
                            onValueChange = {
                                viewModel.updateLastname(it)
                            })

                    }

                    Button(
                        onClick = { viewModel.saveProfileChanges() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        enabled = (state.profile.firstname != null && state.profile.lastname != null)
                    ) {

                        Text(
                            text = "Save changes",
                            modifier = Modifier.padding(4.dp),
                            color = MaterialTheme.colors.surface,
                        )
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
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
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