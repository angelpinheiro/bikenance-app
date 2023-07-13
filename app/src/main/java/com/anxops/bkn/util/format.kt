package com.anxops.bkn.util

import java.text.DecimalFormat

fun formatDistanceAsKm(distanceInMeters: Int) =
    DecimalFormat("#,###.##").format(distanceInMeters.div(1000f)) + "km"