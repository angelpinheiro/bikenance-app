package com.anxops.bkn.ui.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.anxops.bkn.data.model.StatusLevel
import com.anxops.bkn.ui.theme.statusDanger
import com.anxops.bkn.ui.theme.statusGood
import com.anxops.bkn.ui.theme.statusOk
import com.anxops.bkn.ui.theme.statusWarning
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.community.material.CommunityMaterial

@Composable
fun getColorForProgress(percentage: Float, threshold: Float = 0f): Color {
    if (percentage <= threshold) {
        return Color.Transparent
    }
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

@Composable
fun getColorForStatus(statusLevel: StatusLevel?): Color {
    return when (statusLevel) {
        StatusLevel.DANGER -> MaterialTheme.colors.statusDanger
        StatusLevel.WARN -> MaterialTheme.colors.statusWarning
        StatusLevel.OK -> MaterialTheme.colors.statusOk
        StatusLevel.GOOD -> MaterialTheme.colors.statusGood
        else -> MaterialTheme.colors.primary
    }
}

@Composable
fun getIconResForStatus(statusLevel: StatusLevel?): IIcon {
    return when (statusLevel) {
        StatusLevel.DANGER -> CommunityMaterial.Icon3.cmd_progress_wrench
        StatusLevel.WARN -> CommunityMaterial.Icon3.cmd_progress_alert
        StatusLevel.OK -> CommunityMaterial.Icon3.cmd_progress_check
        StatusLevel.GOOD -> CommunityMaterial.Icon3.cmd_progress_check
        else -> CommunityMaterial.Icon3.cmd_progress_question
    }
}