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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStatus
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusOk
import com.anxops.bkn.ui.theme.statusWarning
import kotlinx.coroutines.delay

data class OffsetAndAlign(val x: Float, val y: Float, val align: Alignment = Alignment.TopStart)

@Composable
fun BikeStatusMap(
    modifier: Modifier = Modifier,
    bike: Bike,
    bikeStatus: BikeStatus,
    highlightCategories: Boolean = false,
    highlightedGroup: ComponentCategory? = ComponentCategory.WHEELS,
    onCategorySelected: (ComponentCategory) -> Unit = {}
) {

    val showComponentGroupsFlag = remember {
        mutableStateOf(false)
    }



    LaunchedEffect(Unit) {
        delay(500)
        showComponentGroupsFlag.value = true
    }

    val vector = if (bike.type == BikeType.FULL_MTB) {
        R.drawable.bike_full_mtb
    } else if (bike.type == BikeType.ROAD || bike.type == BikeType.GRAVEL) {
        R.drawable.bike_road
    } else if (bike.type == BikeType.MTB) {
        R.drawable.bike_mtb
    } else {
        R.drawable.bike_mtb
    }

    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {

        BoxWithConstraints(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .aspectRatio(1.5f)
        ) {

            Image(
                imageVector = ImageVector.vectorResource(id = vector),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 3.dp, start = 3.dp)
                    .blur(3.dp)
                    .align(Alignment.Center)
                    .alpha(0.3f),
                colorFilter = ColorFilter.tint(Color.Black)
            )
            Image(
                imageVector = ImageVector.vectorResource(id = vector),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
            )




            HotSpotAnimatedVisibility(
                visible = showComponentGroupsFlag.value && highlightCategories
            ) {
                HotSpot(text = "Transmission",
                    color = getColorForStatus(bikeStatus.componentCategoryStatus[ComponentCategory.TRANSMISSION]),
                    size = 35.dp,
                    maxHeight = maxHeight,
                    maxWidth = maxWidth,
                    xOffset = 0.4f,
                    yOffset = 0.65f,
                    textAlignment = Alignment.BottomCenter,
                    onSelected = { onCategorySelected(ComponentCategory.TRANSMISSION) })

                HotSpot(text = "Misc",
                    color = getColorForStatus(bikeStatus.componentCategoryStatus[ComponentCategory.MISC]),
                    size = 35.dp,
                    maxHeight = maxHeight,
                    maxWidth = maxWidth,
                    xOffset = 0.4f,
                    yOffset = 0.2f,
                    textAlignment = Alignment.TopEnd,
                    onSelected = { onCategorySelected(ComponentCategory.MISC) })

                HotSpot(text = "Brakes",
                    color = getColorForStatus(bikeStatus.componentCategoryStatus[ComponentCategory.BRAKES]),
                    size = 35.dp,
                    maxHeight = maxHeight,
                    maxWidth = maxWidth,
                    xOffset = 0.16f,
                    yOffset = 0.57f,
                    textAlignment = Alignment.TopStart,
                    onSelected = { onCategorySelected(ComponentCategory.BRAKES) })


                HotSpot(text = "Suspension",
                    color = MaterialTheme.colors.statusGood,
                    size = 35.dp,
                    maxHeight = maxHeight,
                    maxWidth = maxWidth,
                    xOffset = 0.75f,
                    yOffset = 0.40f,
                    onSelected = { onCategorySelected(ComponentCategory.SUSPENSION) })



                HotSpot(text = "Tires",
                    color = getColorForStatus(bikeStatus.componentCategoryStatus[ComponentCategory.WHEELS]),
                    size = 35.dp,
                    maxHeight = maxHeight,
                    maxWidth = maxWidth,
                    xOffset = 0.65f,
                    yOffset = 0.70f,
                    textAlignment = Alignment.BottomEnd,
                    onSelected = { onCategorySelected(ComponentCategory.WHEELS) })
            }

            if (!highlightCategories) {

                bike.components?.forEach {
                    val offsets: List<OffsetAndAlign> = when (it.type) {
                        ComponentTypes.BRAKE_LEVER -> {
                            listOf(OffsetAndAlign(0.7f, 0.15f))
                        }

                        ComponentTypes.CABLE_HOUSING -> {
                            listOf(OffsetAndAlign(0.5f, 0.32f, Alignment.TopEnd))
                        }

                        ComponentTypes.CASSETTE -> {
                            listOf(OffsetAndAlign(0.2f, 0.6f))
                        }

                        ComponentTypes.CHAIN -> {
                            listOf(OffsetAndAlign(0.45f, 0.65f))
                        }

                        ComponentTypes.DISC_BRAKE -> {
                            listOf(OffsetAndAlign(0.15f, 0.6f))
                        }

                        ComponentTypes.DISC_PAD -> {
                            listOf(OffsetAndAlign(0.25f, 0.65f, Alignment.BottomCenter))
                        }

                        ComponentTypes.DROPER_POST -> {
                            listOf(OffsetAndAlign(0.35f, 0.2f))
                        }

                        ComponentTypes.FORK -> {
                            listOf(OffsetAndAlign(0.77f, 0.5f))
                        }

                        ComponentTypes.FRONT_HUB -> {
                            listOf(OffsetAndAlign(0.8f, 0.61f, Alignment.CenterEnd))
                        }

                        ComponentTypes.PEDAL_CLIPLESS -> {
                            listOf(OffsetAndAlign(0.5f, 0.7f, Alignment.BottomCenter))
                        }

                        ComponentTypes.REAR_DERAUILLEURS -> {
                            listOf(OffsetAndAlign(0.25f, 0.72f, Alignment.BottomCenter))
                        }

                        ComponentTypes.REAR_HUB -> {
                            listOf(OffsetAndAlign(0.2f, 0.61f, Alignment.CenterStart))
                        }

                        ComponentTypes.REAR_SUSPENSION -> {
                            listOf(OffsetAndAlign(0.45f, 0.45f))
                        }

                        ComponentTypes.THRU_AXLE -> {
                            listOf(
                                OffsetAndAlign(0.2f, 0.62f), OffsetAndAlign(0.8f, 0.62f)
                            )
                        }

                        ComponentTypes.TIRE -> {
                            listOf(
                                OffsetAndAlign(0.2f, 0.35f), OffsetAndAlign(0.8f, 0.35f)
                            )
                        }

                        ComponentTypes.WHEELSET -> {
                            listOf(
                                OffsetAndAlign(0.35f, 0.75f, Alignment.BottomStart),
                                OffsetAndAlign(0.65f, 0.75f, Alignment.BottomEnd)
                            )
                        }

                        else -> {
                            listOf(OffsetAndAlign(0.5f, 0.5f))
                        }
                    }

                    offsets.forEach { offset ->
                        HotSpotAnimatedVisibility(highlightedGroup == it.type.category) {
                            SmallHotSpot(
                                text = stringResource(id = it.type.resources().nameResId),
                                color = getColorForStatus(bikeStatus.componentTypeStatus[it.type]),
                                size = 25.dp,
                                maxHeight = maxHeight,
                                maxWidth = maxWidth,
                                xOffset = offset.x,
                                yOffset = offset.y,
                                textAlignment = offset.align
                            )
                        }
                    }
                }

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
    textAlignment: Alignment = Alignment.TopEnd,
    onSelected: () -> Unit = {}
) {

    val boxSize = max(80.dp, size)

    val top = max(maxHeight * yOffset - boxSize / 2, 0.dp)
    val start = max(maxWidth * xOffset - boxSize / 2, 0.dp)

    Box(
        modifier
            .padding(top = top, start = start)
            .size(boxSize)
            .clickable { onSelected() },
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
                .background(MaterialTheme.colors.surface.copy(alpha = 0.2f))
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


@Composable
fun SmallHotSpot(
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

    val boxSize = max(50.dp, size)

    val top = max(maxHeight * yOffset - boxSize / 2, 0.dp)
    val start = max(maxWidth * xOffset - boxSize, 0.dp)

    Box(
        modifier
            .padding(top = top, start = start)
            .width(boxSize * 2)
            .height(boxSize),
        contentAlignment = Alignment.Center

    ) {

        PulsatingCircles(size, color = color, alpha = 0.2f)
        Text(
            text = text,
            Modifier
                .align(textAlignment)
                .padding(horizontal = 6.dp, vertical = 1.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h4,
            fontSize = 12.sp
        )
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    BikenanceAndroidTheme(useSystemUIController = false, darkTheme = false) {
//        BikeStatusMap(
//            bike = Bike(
//                "",
//                "",
//                brandName = "Scott",
//                configDone = false,
//                name = "MySpark",
//                distance = 10000,
//                draft = false,
//                modelName = "Spark",
//                electric = false,
//                photoUrl = null,
//                stravaId = "",
//                type = BikeType.FULL_MTB
//            ), modifier = Modifier.fillMaxWidth(0.9f)
//        )
//    }
//}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HotSpotAnimatedVisibility(
    visible: Boolean, content: @Composable() AnimatedVisibilityScope.() -> Unit
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
        modifier = Modifier.wrapContentSize(Alignment.Center)
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
fun PulsatingCircles(size: Dp, color: Color = Color.White, alpha: Float = 0.3f) {
    Box(contentAlignment = Alignment.Center) {
        val infiniteTransition = rememberInfiniteTransition(label = "")

        val alpha by infiniteTransition.animateValue(
            label = "",
            initialValue = alpha,
            targetValue = 0.1f,
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
            ),
            label = ""
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(size),
            contentAlignment = Alignment.Center,
        ) {
            SimpleCircleShape2(
                size = size, color = color.copy(alpha = alpha)
            )
            SimpleCircleShape2(
                size = smallCircle, color = color.copy(alpha = alpha)
            )
            SimpleCircleShape2(
                size = size * .4f, color = color.copy(alpha = 0.9f)
            )
        }
    }
}