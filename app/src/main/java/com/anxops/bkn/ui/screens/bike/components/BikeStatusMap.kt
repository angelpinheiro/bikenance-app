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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.anxops.bkn.R
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.data.model.ComponentCategory
import com.anxops.bkn.data.model.ComponentModifier
import com.anxops.bkn.data.model.ComponentType
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.screens.maintenances.getColorForStatus
import com.anxops.bkn.ui.screens.maintenances.getIconResForStatus
import com.anxops.bkn.ui.shared.BikeComponentIcon
import com.anxops.bkn.ui.shared.components.BknIcon
import com.anxops.bkn.ui.shared.resources
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusWarning
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import kotlinx.coroutines.delay

data class MapOffset(val x: Float, val y: Float, val align: Alignment = Alignment.TopStart)

@Composable
fun BikeStatusMap(
    bike: Bike,
    highlightCategories: Boolean = false,
    selectedCategory: ComponentCategory? = ComponentCategory.WHEELS,
    selectedComponent: BikeComponent? = null,
    onCategorySelected: (ComponentCategory) -> Unit = {},
    onComponentSelected: (BikeComponent) -> Unit = {},
    onCategoryUnselected: () -> Unit = {}
) {

    val showComponentGroupsFlag = remember {
        mutableStateOf(false)
    }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        delay(100)
        showComponentGroupsFlag.value = true
    }

    val vector = when (bike.type) {
        BikeType.Mtb -> {
            if (bike.fullSuspension) R.drawable.bike_full_mtb else R.drawable.bike_mtb
        }

        BikeType.Road, BikeType.Gravel -> {
            R.drawable.bike_road
        }

        else -> {
            R.drawable.bike_mtb
        }
    }

    BoxWithConstraints(modifier = Modifier
        .padding(horizontal = 26.dp)
        .fillMaxWidth()
        .aspectRatio(1.3f)
        .clickable(
            interactionSource = interactionSource, indication = null

        ) { onCategoryUnselected() }) {

        Image(
            imageVector = ImageVector.vectorResource(id = vector),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(5.dp)
                .padding(5.dp)
                .align(Alignment.Center)
                .alpha(0.5f),
            colorFilter = ColorFilter.tint(Color.Black)
        )
        Image(
            imageVector = ImageVector.vectorResource(id = vector),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .align(Alignment.Center),
            colorFilter = ColorFilter.tint(Color.hsl(227f, 0.24f, 0.29f))
        )

        HotSpotAnimatedVisibility(
            visible = showComponentGroupsFlag.value && highlightCategories
        ) {
            HotSpot(text = stringResource(id = ComponentCategory.TRANSMISSION.resources().nameResId),
                color = getColorForStatus(bike.status.componentCategoryStatus[ComponentCategory.TRANSMISSION]),
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.4f,
                yOffset = 0.65f,
                textAlignment = Alignment.BottomCenter,
                onSelected = { onCategorySelected(ComponentCategory.TRANSMISSION) })

            HotSpot(text = stringResource(id = ComponentCategory.MISC.resources().nameResId),
                color = getColorForStatus(bike.status.componentCategoryStatus[ComponentCategory.MISC]),
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.4f,
                yOffset = 0.2f,
                textAlignment = Alignment.TopEnd,
                onSelected = { onCategorySelected(ComponentCategory.MISC) })

            HotSpot(text = stringResource(id = ComponentCategory.BRAKES.resources().nameResId),
                color = getColorForStatus(bike.status.componentCategoryStatus[ComponentCategory.BRAKES]),
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.16f,
                yOffset = 0.57f,
                textAlignment = Alignment.TopStart,
                onSelected = { onCategorySelected(ComponentCategory.BRAKES) })


            HotSpot(text = stringResource(id = ComponentCategory.SUSPENSION.resources().nameResId),
                color = MaterialTheme.colors.statusGood,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.75f,
                yOffset = 0.40f,
                onSelected = { onCategorySelected(ComponentCategory.SUSPENSION) })

            HotSpot(text = stringResource(id = ComponentCategory.WHEELS.resources().nameResId),
                color = getColorForStatus(bike.status.componentCategoryStatus[ComponentCategory.WHEELS]),
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                xOffset = 0.65f,
                yOffset = 0.70f,
                textAlignment = Alignment.BottomEnd,
                onSelected = { onCategorySelected(ComponentCategory.WHEELS) })
        }

        if (!highlightCategories) {

            bike.components.forEach { component ->

                val isSelected = component._id == selectedComponent?._id

                val offset: MapOffset = when (component.type) {
                    ComponentType.BrakeLever -> MapOffset(0.7f, 0.15f)

                    ComponentType.CableHousing -> MapOffset(0.5f, 0.32f, Alignment.TopEnd)

                    ComponentType.Cassette -> MapOffset(0.2f, 0.6f)

                    ComponentType.Chain -> MapOffset(0.37f, 0.55f)

                    ComponentType.DropperPost -> MapOffset(0.35f, 0.2f)

                    ComponentType.Fork -> MapOffset(0.77f, 0.5f)

                    ComponentType.FrontHub -> MapOffset(0.8f, 0.61f, Alignment.CenterEnd)

                    ComponentType.PedalClipless -> MapOffset(0.5f, 0.7f, Alignment.BottomCenter)

                    ComponentType.RearDerailleurs -> MapOffset(0.28f, 0.74f, Alignment.BottomCenter)

                    ComponentType.RearHub -> MapOffset(0.2f, 0.61f, Alignment.CenterStart)

                    ComponentType.RearSuspension -> MapOffset(0.45f, 0.45f)

                    ComponentType.ChainRing -> MapOffset(0.5f, 0.62f)

                    ComponentType.ThruAxle -> {
                        if (component.modifier == ComponentModifier.REAR) {
                            MapOffset(0.2f, 0.62f)
                        } else {
                            MapOffset(0.8f, 0.62f)
                        }
                    }

                    ComponentType.Tire -> {
                        if (component.modifier == ComponentModifier.REAR) {
                            MapOffset(0.2f, 0.35f)
                        } else {
                            MapOffset(0.8f, 0.35f)
                        }
                    }

                    ComponentType.Wheel -> {
                        if (component.modifier == ComponentModifier.REAR) {
                            MapOffset(0.25f, 0.75f, Alignment.BottomStart)
                        } else {
                            MapOffset(0.65f, 0.75f, Alignment.BottomEnd)
                        }
                    }

                    ComponentType.DiscPad -> {
                        if (component.modifier == ComponentModifier.REAR) {
                            MapOffset(0.3f, 0.48f)
                        } else {
                            MapOffset(0.7f, 0.48f, Alignment.BottomEnd)
                        }
                    }

                    ComponentType.DiscBrake -> {
                        if (component.modifier == ComponentModifier.REAR) {
                            MapOffset(0.2f, 0.6f, Alignment.BottomCenter)
                        } else {
                            MapOffset(0.8f, 0.6f)
                        }
                    }


                    ComponentType.FrameBearings -> MapOffset(0f, 0f)
                    ComponentType.HandlebarTape -> MapOffset(0.5f, 0.25f)
                    ComponentType.Custom -> MapOffset(0f, 0f)
                }


                HotSpotAnimatedVisibility(selectedCategory == component.type.category) {
                    StatusMapComponent(isSelected = isSelected,
                        size = 25.dp,
                        maxHeight = maxHeight,
                        maxWidth = maxWidth,
                        xOffset = offset.x,
                        yOffset = offset.y,
                        component = component,
                        status = bike.status.componentTypeStatus[component.type],
                        onSelected = {
                            onComponentSelected(component)
                        })
                }

            }

        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComponentCategoryCarousel(
    selectedCategory: ComponentCategory?, onCategorySelected: (ComponentCategory) -> Unit
) {

    val categoryScroll = rememberLazyListState()
    val categories = remember {
        ComponentCategory.values()
    }

    LaunchedEffect(selectedCategory) {
        val index = categories.indexOf(selectedCategory)
        if (index > 0) categoryScroll.animateScrollToItem(index)
    }

    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        state = categoryScroll,
        horizontalArrangement = Arrangement.Center
    ) {
        categories.forEach { category ->

            val isSelected = selectedCategory == category
            item {
                Chip(
                    modifier = Modifier
                        .widthIn(80.dp, 180.dp)
                        .padding(horizontal = 3.dp),
                    onClick = { onCategorySelected(category) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.primary,
                        contentColor = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary
                    ),
                    leadingIcon = {
                        BknIcon(
                            icon = CommunityMaterial.Icon.cmd_close,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .size(10.dp),
                            color = if (isSelected) MaterialTheme.colors.surface else Color.Transparent

                        )
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 16.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = category.resources().nameResId),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComponentCarousel(
    components: List<BikeComponent>,
    selectedComponent: BikeComponent?,
    onComponentSelected: (BikeComponent) -> Unit
) {

    val categoryScroll = rememberLazyListState()

    LaunchedEffect(selectedComponent) {
        val index = components.indexOf(selectedComponent)
        if (index > 0) categoryScroll.animateScrollToItem(index)
    }

    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        state = categoryScroll,
        horizontalArrangement = Arrangement.Center
    ) {
        components.forEach { component ->

            val isSelected = selectedComponent?._id == component._id

            item {
                Chip(
                    modifier = Modifier
                        .widthIn(80.dp, 180.dp)
                        .padding(start = 6.dp),
                    onClick = { onComponentSelected(component) },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
                        contentColor = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface
                    ),
                    leadingIcon = {
                        BikeComponentIcon(
                            type = component.type,
                            tint = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .size(16.dp)
                        )
                    },
                ) {
                    Text(
                        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp, end = 16.dp),
                        textAlign = TextAlign.Center,
                        text = (component.modifier?.displayName?.plus(" ") ?: "") + stringResource(
                            id = component.type.resources().nameResId
                        ),
                        color = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.h4,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
    size: Dp = 45.dp,
    xOffset: Float,
    yOffset: Float,
    maxWidth: Dp,
    maxHeight: Dp,
    textAlignment: Alignment = Alignment.TopEnd,
    onSelected: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val boxSize = max(80.dp, size)

    val top = max(maxHeight * yOffset - boxSize / 2, 0.dp)
    val start = max(maxWidth * xOffset - boxSize / 2, 0.dp)

    Box(
        modifier
            .padding(top = top, start = start)
            .size(boxSize)
            .clickable(interactionSource, indication = null) { onSelected() },
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
fun StatusMapComponent(
    component: BikeComponent,
    isSelected: Boolean = false,
    status: StatusLevel? = StatusLevel.UNKNOWN,
    modifier: Modifier = Modifier,
    size: Dp,
    xOffset: Float,
    yOffset: Float,
    maxWidth: Dp,
    maxHeight: Dp,
    onSelected: () -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val boxSize = max(50.dp, size)

    val top = max(maxHeight * yOffset - boxSize / 2, 0.dp)
    val start = max(maxWidth * xOffset - boxSize / 2, 0.dp)


    val color = getColorForStatus(status ?: StatusLevel.UNKNOWN)
    val icon = getIconResForStatus(status ?: StatusLevel.UNKNOWN)

    Box(
        modifier
            .padding(top = top, start = start)
            .width(boxSize)
            .height(boxSize)
            .clickable(interactionSource, indication = null) { onSelected() },
        contentAlignment = Alignment.Center

    ) {

        BikeComponentIcon(
            type = component.type,
            tint = if (isSelected) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
                .padding(5.dp)
        )

        CircularProgressIndicator(
            modifier = Modifier.size(42.dp), color = color, progress = component.status.toFloat()
        )
    }

}

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
fun SimpleCircleShape(
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
            targetValue = 0.3f,
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
            SimpleCircleShape(
                size = size, color = color.copy(alpha = alpha)
            )
            SimpleCircleShape(
                size = smallCircle, color = color.copy(alpha = alpha)
            )
            SimpleCircleShape(
                size = size * .4f, color = color.copy(alpha = 0.9f)
            )
        }
    }
}