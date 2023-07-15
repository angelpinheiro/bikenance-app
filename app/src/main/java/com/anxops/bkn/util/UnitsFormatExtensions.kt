package com.anxops.bkn.util

import java.text.DecimalFormat

fun formatDistanceAsKm(distanceInMeters: Int?) =
    distanceInMeters?.let { DecimalFormat("#,###.##").format(it.div(1000f)) + " km" } ?: "-- km"


fun Number.formatAsDistanceInKm() {
    formatDistanceAsKm(this.toInt())
}