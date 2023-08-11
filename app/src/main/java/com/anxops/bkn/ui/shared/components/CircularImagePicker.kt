package com.anxops.bkn.ui.shared.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.anxops.bkn.ui.theme.statusGood

@Composable
fun CircularImagePicker(url: String?, defaultImageResId: Int, onError: () -> Unit = {}, onUpdate: (ByteArray) -> Unit) {
    val context = LocalContext.current

    val progress = remember {
        mutableFloatStateOf(0f)
    }

    // Result for launching photo picker
    val pickMedia = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
        if (uri != null) {
            context.contentResolver.openInputStream(uri)?.use {
                onUpdate(it.readBytes())
            }
        }
    })

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .memoryCacheKey(url)
        .diskCacheKey(url)
        .error(defaultImageResId)
        .fallback(defaultImageResId)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .build()

    Box {
        IconButton(onClick = {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            SubcomposeAsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier.size(100.dp).padding(0.dp).clip(CircleShape).background(MaterialTheme.colors.background),
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        strokeWidth = 5.dp,
                        color = MaterialTheme.colors.statusGood
                    )
                },
                onError = { onError() }

            )
        }
    }
}
