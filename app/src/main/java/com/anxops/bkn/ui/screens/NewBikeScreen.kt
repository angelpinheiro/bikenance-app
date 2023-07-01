package com.anxops.bkn.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.anxops.bkn.model.BikeType
import com.anxops.bkn.ui.shared.BknLabelTopTextField
import com.anxops.bkn.ui.shared.Loading
import com.anxops.bkn.ui.shared.onSurfaceTextFieldColors
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import java.text.DecimalFormat

@Destination
@Composable
fun NewBikeScreen(
    navigator: DestinationsNavigator,
    viewModel: NewBikeScreenViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>,
    bikeId: String = ""
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()

    if (bikeId.isNotBlank()) {
        viewModel.loadBike(bikeId)
    }

    val isNew = viewModel.isNewBike().collectAsState(true).value


    val imageData = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = (ActivityResultContracts.GetContent()),
        onResult = { uri ->
            imageData.value = uri
            uri?.let {
                val inputStream = context.contentResolver.openInputStream(it)
                val imageByteArray = inputStream?.readBytes()
                viewModel.updateBikeImage(imageByteArray)
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
        NewBikeScreenStatus.Loading -> {
            Loading()
        }

        NewBikeScreenStatus.Saving -> {
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
                    text = if (isNew) "Lets create a new Bike!" else "${state.value.bike.name}",
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier.padding(start = 10.dp),
                    color = MaterialTheme.colors.onSurface
                )

                Text(
                    text = if (isNew) "Enter your bike details below" else "Edit your bike details below",
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
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(state.value.bike.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(0.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.background),
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

                    BknLabelTopTextField(
                        value = state.value.bike.name,
                        label = "Alias",
                        colors = colors,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = {
                            viewModel.updateName(it)

                        })

                    BknLabelTopTextField(value = state.value.bike.brandName,
                        label = "Brand name",
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors,
                        onValueChange = {
                            viewModel.updateBrandName(it)
                        })

                    BknLabelTopTextField(value = state.value.bike.modelName,
                        label = "Model name",
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors,
                        onValueChange = {
                            viewModel.updateModel(it)
                        })

                    BknLabelTopTextField(
                        value = (state.value.bike.km()?.toString()),
                        label = "Distance",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = colors,
                        onValueChange = {
                            if (it.length <= 10) {
                                if (it.isNotBlank())
                                    viewModel.updateDistance(it.toLong())
                                else
                                    viewModel.updateDistance(null)
                            }
                        },
                        trailingIcon = {
                            Text(
                                text = "km",
                                modifier = Modifier.padding(horizontal = 20.dp),
                                style = MaterialTheme.typography.h3
                            )
                        },
                        visualTransformation = AmountOrMessageVisualTransformation()
                    )

                    Text(
                        text = "Bike type",
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.padding(vertical = 0.dp, horizontal = 10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {


                        BikeType.values().filter { it != BikeType.UNKNOWN }.forEach { type ->
                            val selected = type == state.value.bike.type
                            Button(
                                modifier = Modifier.padding(horizontal = 5.dp),
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.background
                                ),
                                onClick = { viewModel.updateBikeType(type) },
                            ) {
                                Text(
                                    type.type,
                                    color = if (selected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                                    style = MaterialTheme.typography.h5
                                )
                            }
                        }
                    }
                }


                Button(
                    onClick = { viewModel.onSaveBike() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    enabled = (state.value.bike.name != null)
                ) {

                    Text(text = "Save changes", modifier = Modifier.padding(4.dp))
                }
                if (!isNew) {
                    Button(
                        onClick = { viewModel.deleteBike() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.statusDanger),
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                    ) {

                        Text(
                            text = "Delete this bike",
                            modifier = Modifier.padding(4.dp),
                            color = MaterialTheme.colors.onError
                        )
                    }
                }

            }
        }
    }

}

val nullTranslator = object : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return offset
    }

    override fun transformedToOriginal(offset: Int): Int {
        return offset
    }
}

class AmountOrMessageVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {

        if (text.text.isNullOrBlank() || text.text.isEmpty()) {
            return TransformedText(AnnotatedString(""), nullTranslator)
        }

        val originalText = text.text.trim()
        val formattedText = DecimalFormat("#,###").format(text.text.toDouble())

        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isNotEmpty()) {
                    val commas =
                        formattedText.subSequence(0, offset).count { it == '.' || it == ',' }
                    return offset + commas
                }
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (originalText.isNotEmpty()) {
                    val commas =
                        formattedText.subSequence(0, offset).count { it == '.' || it == ',' }
                    return offset - commas
                }
                return offset
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}