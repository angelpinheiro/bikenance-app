package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Calendar
import java.util.Date

@Serializable
data class AthleteStats(
    @SerialName("biggest_ride_distance") val biggestRideDistance: Double,
    @SerialName("biggest_climb_elevation_gain") val biggestClimbElevationGain: Double,
    @SerialName("recent_ride_totals") val recentRideTotals: ActivityTotal,
    @SerialName("ytd_ride_totals") val ytdRideTotals: ActivityTotal,
    @SerialName("all_ride_totals") val allRideTotals: ActivityTotal,
) {
    fun recentAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = recentRideTotals.count,
            duration = recentRideTotals.duration / recentRideTotals.count,
            distance = recentRideTotals.distance / recentRideTotals.count,
            elevationGain = recentRideTotals.elevationGain / recentRideTotals.count,
            movingTime = recentRideTotals.movingTime / recentRideTotals.count
        )
    }

    fun totalsAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = allRideTotals.count,
            duration = allRideTotals.duration / allRideTotals.count,
            distance = allRideTotals.distance / allRideTotals.count,
            elevationGain = allRideTotals.elevationGain / allRideTotals.count,
            movingTime = allRideTotals.movingTime / allRideTotals.count
        )
    }

    fun ytdAverageActivity(): ActivityTotal {
        return ActivityTotal(
            count = ytdRideTotals.count,
            duration = ytdRideTotals.duration / ytdRideTotals.count,
            distance = ytdRideTotals.distance / ytdRideTotals.count,
            elevationGain = ytdRideTotals.elevationGain / ytdRideTotals.count,
            movingTime = ytdRideTotals.movingTime / ytdRideTotals.count
        )
    }

    fun ridesPerYear(): Double {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val daysFromStartOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        return 365f * ytdRideTotals.count / daysFromStartOfYear
    }

    fun monthlyEstimationBasedOnYTD(): Pair<Usage, ActivityTotal> {

        val ridesPerYear = 365f * ytdRideTotals.count / ridesPerYear()
        val ridesPerMonth = ridesPerYear / 12

        val avgDistance = ytdRideTotals.distance / ytdRideTotals.count
        val avgDuration = ytdRideTotals.duration / ytdRideTotals.count
        val avgElevation = ytdRideTotals.elevationGain / ytdRideTotals.count
        val avgMovingTime = ytdRideTotals.movingTime / ytdRideTotals.count

        val monthlyDistance = ridesPerMonth * avgDistance
        val monthlyDurationHours = ridesPerMonth * avgDuration / 3600

        return Usage(monthlyDurationHours, monthlyDistance) to ActivityTotal(
            count = ridesPerMonth,
            duration = avgDuration,
            distance = avgDistance,
            elevationGain = avgElevation,
            movingTime = avgMovingTime
        )
    }
}