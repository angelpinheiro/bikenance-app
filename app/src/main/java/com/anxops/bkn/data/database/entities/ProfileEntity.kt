package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.Profile

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "user_name") val username: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "profile_photo") val profilePhoto: String?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,

    @ColumnInfo(name = "biggest_ride_distance") val biggestRideDistance: Double = 0.0,
    @ColumnInfo(name = "biggest_climb_elevation_gain") val biggestClimbElevationGain: Double = 0.0,
    @ColumnInfo(name = "recent_ride_total_distance") val recentRideTotalDistance: Double = 0.0,
    @ColumnInfo(name = "recent_ride_total_duration") val recentRideTotalDuration: Double = 0.0,
    @ColumnInfo(name = "ytd_ride_total_distance") val ytdRideTotalDistance: Double = 0.0,
    @ColumnInfo(name = "ytd_ride_totals_duration") val ytdRideTotalDuration: Double = 0.0,
    @ColumnInfo(name = "all_ride_totals_distance") val allRideTotalDistance: Double = 0.0,
    @ColumnInfo(name = "all_ride_total_duration") val allRideTotalDuration: Double = 0.0,

    ) {
    fun toDomain(): Profile {
        return Profile(
            _id = _id ?: "",
            userId = userId,
            username = username,
            firstname = firstName,
            lastname = lastName,
            profilePhotoUrl = profilePhoto,
            sex = null,
            weight = null,
            createdAt = createdAt,

//            biggestRideDistance = biggestRideDistance,
//            biggestClimbElevationGain = biggestClimbElevationGain,
//            recentRideTotalDistance = recentRideTotalDistance,
//            recentRideTotalDuration = recentRideTotalDuration,
//            ytdRideTotalDistance = ytdRideTotalDistance,
//            ytdRideTotalDuration = ytdRideTotalDuration,
//            allRideTotalDistance = allRideTotalDistance,
//            allRideTotalDuration = allRideTotalDuration
        )
    }
}