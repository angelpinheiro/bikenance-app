package com.anxops.bkn.ui.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.R
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.backgroundBox

@Composable
fun BackgroundBox(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colors.backgroundBox),
        contentAlignment = contentAlignment
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.polygon_bg_alt),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.05f)
        )
        content()
    }
}

@Preview
@Composable
fun BackgroundBoxPreview() {
    BikenanceAndroidTheme {
        BackgroundBox(
            modifier = Modifier.width(600.dp).height(1200.dp)
        ) {}
    }
}

@Preview
@Composable
fun BackgroundBoxPreviewDark() {
    BikenanceAndroidTheme(darkTheme = true) {
        BackgroundBox(
            modifier = Modifier.width(600.dp).height(1200.dp)
        ) {}
    }
}
