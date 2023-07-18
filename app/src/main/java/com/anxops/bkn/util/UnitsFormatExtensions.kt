package com.anxops.bkn.util

import java.text.DecimalFormat

fun formatDistanceAsKm(distanceInMeters: Int?) =
    distanceInMeters?.let { DecimalFormat("#,###.##").format(it.div(1000f)) + " km" } ?: "-- km"

fun formatDistanceAsShortKm(distanceInMeters: Int?) =
    distanceInMeters?.let { DecimalFormat("#,###").format(it.div(1000f * 1000f)) + "M km" } ?: "-- km"

fun formatElevation(elevationInMeters: Int?) =
    elevationInMeters?.let { DecimalFormat("#,###").format(it) + " m" } ?: "-- m"


fun Number.formatAsDistanceInKm() {
    formatDistanceAsKm(this.toInt())
}