package com.anxops.bkn.ui.screens.bike.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.MaintenanceConfigurations

@Composable
fun ComponentListBottomSheet(
    selectedComponents: Set<ComponentTypes>,
    selectable: Boolean = false,
    onSelectionChanged: (Set<ComponentTypes>) -> Unit = {},
    onSelectConfiguration: (MaintenanceConfigurations) -> Unit = {},
    onDone: () -> Unit = {},

    ) {

    val componentTypes = remember { ComponentTypes.values() }
    val lazyColumnState = rememberLazyListState()
    val bottomPartSize = 60.dp

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
    ) {


        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPartSize)
                .background(MaterialTheme.colors.surface)
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                ) {

                    Text(
                        text = "Check the components you want to keep track, or select one of the options below to let us do it for you",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.primary
                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        MaintenanceConfigurations.values().forEach {
                            OutlinedButton(
                                onClick = {
                                    onSelectConfiguration(it)
                                },
                                modifier = Modifier.padding(4.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                            ) {
                                Text(
                                    text = it.configName,
                                    color = MaterialTheme.colors.onSecondary,
                                    style = MaterialTheme.typography.h5,
                                )
                            }
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                    }


                }

            }

            items(count = componentTypes.size) { index ->
                val type = componentTypes[index]
                val selected = selectedComponents.contains(type)

                BottomSheetComponentItem(type = type,
                    selectable = selectable,
                    selected = selected,
                    onClick = {
                        if (selectedComponents.contains(type))
                            onSelectionChanged(selectedComponents.minus(type))
                        else
                            onSelectionChanged(selectedComponents.plus(type))
                    })

            }
        }
        Box(
            Modifier
                .fillMaxWidth()
//                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(MaterialTheme.colors.primaryVariant)
                .height(bottomPartSize)
                .padding(0.dp)
                .align(Alignment.BottomCenter)
        ) {
            OutlinedButton(onClick = { onDone() }, Modifier.align(Alignment.Center)) {
                Text(
                    text = "Done",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h5,
                )
            }
        }

    }
}