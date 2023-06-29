package com.anxops.bkn.ui.components

import android.widget.ProgressBar
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anxops.bkn.storage.FakeData
import com.anxops.bkn.ui.screens.SplashScreen
import com.anxops.bkn.ui.shared.BknIcon
import com.anxops.bkn.ui.theme.BikenanceAndroidTheme
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusOk
import com.anxops.bkn.ui.theme.statusWarning
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator


data class MaintenanceItem(
    val bike: String,
    val bikePart: String,
    val title: String,
    val time: String,
    val percentage: Float
)

@Composable
fun MaintenanceItemView(item: MaintenanceItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 5.dp)
            .background(MaterialTheme.colors.surface)
    ) {
        Row {
            LinearProgressIndicator(
                progress = item.percentage,
                color = getColorForProgress(percentage = item.percentage).copy(alpha = 0.9f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    BknIcon(
                        CommunityMaterial.Icon3.cmd_tools,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(16.dp)
                    )
                    Text(
                        item.bikePart,
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                Text(
                    item.title,
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colors.background, shape = CircleShape
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
                Text(
                    item.time,
                    style = MaterialTheme.typography.h3,
                )
            }
//            Column(
//                Modifier
//                    .fillMaxHeight()
//                    .padding(top = 10.dp)
//                    .align(Alignment.TopEnd),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                CircularProgressIndicator(
//                    progress = item.percentage,
//                    Modifier
//                        .size(25.dp),
//                    color = getColorForProgress(percentage = item.percentage).copy(alpha = 0.9f),
//                    strokeWidth = 6.dp
//                )
//                Text(
//                    text = (item.percentage * 100).toInt().toString() + "%",
//                    style = MaterialTheme.typography.h3,
//                    color = getColorForProgress(percentage = item.percentage)
//                )
//            }
        }
        Row(
            horizontalArrangement = Arrangement.End, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 10.dp)
        ) {


            OutlinedButton(onClick = {}) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_bike,
                        MaterialTheme.colors.primary,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = "Delay",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            Button(onClick = {}, modifier = Modifier.padding(horizontal = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BknIcon(
                        CommunityMaterial.Icon.cmd_check,
                        MaterialTheme.colors.onSecondary,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }


        }
    }
//    }

}

@Composable
fun getColorForProgress(percentage: Float): Color {
    return when {
        percentage >= 1 -> {
            MaterialTheme.colors.statusDanger
        }

        percentage > 0.8 -> {
            MaterialTheme.colors.statusWarning
        }

        percentage > 0.5 -> {
            MaterialTheme.colors.statusOk
        }

        else -> {
            MaterialTheme.colors.statusGood
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MaintenanceItemPreview() {
    BikenanceAndroidTheme {
        MaintenanceItemView(FakeData.maintenances.first())
    }
}

@Preview(showBackground = false)
@Composable
fun MaintenanceItemPreview2() {
    BikenanceAndroidTheme {
        MaintenanceItemView(FakeData.maintenances[2])
    }
}
