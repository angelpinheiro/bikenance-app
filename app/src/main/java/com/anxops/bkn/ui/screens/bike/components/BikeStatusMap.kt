package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.shared.components.bgGradient
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusOk
import com.anxops.bkn.ui.theme.statusWarning
import kotlinx.coroutines.delay

@Composable
fun BikeStatusMap(modifier: Modifier = Modifier, showComponentGroups: Boolean = true, highlightedGroup: ComponentCategory? = null) {

    val showComponentGroupsFlag = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        delay(1000)
        showComponentGroupsFlag.value = true
    }

    val vector = R.drawable.bike_full_mtb
    BoxWithConstraints(
        modifier = Modifier
            .background(bgGradient())
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .aspectRatio(1.4f)
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = vector),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.surface)
        )

        HotSpotAnimatedVisibility(visible = showComponentGroupsFlag.value) {
            HotSpot(
                text = "Transmission",
                color = MaterialTheme.colors.statusWarning,
                size =  45.dp,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.4f,
                yOffset = 0.65f,
                textAlignment = Alignment.BottomCenter
            )
        }
        HotSpotAnimatedVisibility(showComponentGroupsFlag.value) {
            HotSpot(
                text = "Other",
                color = MaterialTheme.colors.statusGood,
                size =  45.dp,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.4f,
                yOffset = 0.2f,
                textAlignment = Alignment.TopEnd
            )
        }
        HotSpotAnimatedVisibility(showComponentGroupsFlag.value) {
            HotSpot(
                text = "Brakes",
                color = MaterialTheme.colors.statusGood,
                size =  45.dp,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.16f,
                yOffset = 0.57f,
                textAlignment = Alignment.TopStart
            )
        }
        HotSpotAnimatedVisibility(showComponentGroupsFlag.value) {
            HotSpot(
                text = "Suspension",
                color = MaterialTheme.colors.statusGood,
                size =  45.dp,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.75f,
                yOffset = 0.40f
            )
        }

        HotSpotAnimatedVisibility(showComponentGroupsFlag.value) {
            HotSpot(
                text = "Tires",
                color = MaterialTheme.colors.statusOk,
                size =  45.dp,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.65f,
                yOffset = 0.70f,
                textAlignment = Alignment.BottomEnd
            )
        }
        
        ComponentTypes.values().forEach { 
            when(it) {
                ComponentTypes.BRAKE_LEVER -> {}
                ComponentTypes.CABLE_HOUSING -> {}
                ComponentTypes.CASSETTE -> {}
                ComponentTypes.CHAIN -> {}
                ComponentTypes.DISC_BRAKE -> {}
                ComponentTypes.DISC_PAD -> {}
                ComponentTypes.DROPER_POST -> {}
                ComponentTypes.FORK -> {}
                ComponentTypes.FRONT_HUB -> {}
                ComponentTypes.PEDAL_CLIPLESS -> {}
                ComponentTypes.REAR_DERAUILLEURS -> {}
                ComponentTypes.REAR_HUB -> {}
                ComponentTypes.REAR_SUSPENSION -> {}
                ComponentTypes.THRU_AXLE -> {}
                ComponentTypes.TIRE -> {}
                ComponentTypes.WHEELSET -> {}
                ComponentTypes.CUSTOM -> {}
                ComponentTypes.UNKNOWN -> {}
            }
        }
        
    }

}


@Composable
fun HotSpot(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.statusWarning,
    size: Dp,
    xOffset: Float,
    yOffset: Float,
    maxWidth: Dp,
    maxHeight: Dp,
    textAlignment: Alignment = Alignment.TopEnd
) {

    val boxSize = max(80.dp, size)

    val top = maxHeight * yOffset - boxSize / 2
    val start = maxWidth * xOffset - boxSize / 2

    Box(
        modifier
            .padding(top = top, start = start)
            .size(boxSize),
        contentAlignment = Alignment.Center

    ) {

        PulsatingCircles(size, color = color)
        Text(
            text = text,
            Modifier
                .align(textAlignment)
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .background(MaterialTheme.colors.primary)
                .padding(horizontal = 6.dp, vertical = 1.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h4,
            fontSize = 13.sp
        )
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BikenanceAndroidTheme(useSystemUIController = false, darkTheme = false) {
        BikeStatusMap(
            Modifier
                .fillMaxWidth(0.9f)

        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HotSpotAnimatedVisibility(
    visible: Boolean,
    content: @Composable() AnimatedVisibilityScope.() -> Unit
) {

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),

        ) {
        content()
    }
}

@Composable
fun SimpleCircleShape2(
    size: Dp,
    color: Color = Color.White,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.LightGray.copy(alpha = 0.0f)
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    color
                )
                .border(borderWidth, borderColor)
        )
    }
}

@Composable
fun PulsatingCircles(size: Dp, color: Color = Color.White) {
    Box(contentAlignment = Alignment.Center) {
        val infiniteTransition = rememberInfiniteTransition(label = "")

        val alpha by infiniteTransition.animateValue(
            label = "",
            initialValue = 0.5f,
            targetValue = 0.2f,
            typeConverter = Float.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )

        val size by infiniteTransition.animateValue(
            label = "",
            initialValue = size,
            targetValue = size * 0.8f,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        val smallCircle by infiniteTransition.animateValue(
            initialValue = size * 0.7f,
            targetValue = size * 0.4f,
            Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size),
            contentAlignment = Alignment.Center,
        ) {
            SimpleCircleShape2(
                size = size,
                color = color.copy(alpha = alpha)
            )
            SimpleCircleShape2(
                size = smallCircle,
                color = color.copy(alpha = alpha)
            )
            SimpleCircleShape2(
                size = size * .4f,
                color = color.copy(alpha = 0.9f)
            )
        }
    }
}