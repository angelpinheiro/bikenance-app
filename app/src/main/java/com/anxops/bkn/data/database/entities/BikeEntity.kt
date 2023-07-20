package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeStats
import com.anxops.bkn.data.model.BikeType
import com.anxops.bkn.util.toLocalDateTime
import java.time.LocalDateTime

@Entity(tableName = "bike")
data class BikeEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "user_id") val userId: String?,
    @ColumnInfo(name = "strava_id") val stravaId: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "model") val modelName: String?,
    @ColumnInfo(name = "brand") val brandName: String?,
    @ColumnInfo(name = "distance") val distance: Long?,
    @ColumnInfo(name = "photo_url") val photoUrl: String?,
    @ColumnInfo(name = "draft") val draft: Boolean = false,
    @ColumnInfo(name = "electric") val electric: Boolean = false,
    @ColumnInfo(name = "full_suspension") val fullSuspension: Boolean = false,
    @ColumnInfo(name = "configDOne") val configDone: Boolean = false,
    @ColumnInfo(name = "type") val type: String,
    @Embedded val stats : BikeStatsEntity? = null
) {
    fun toDomain(): Bike {
        return Bike(
            _id = _id,
            userId = userId,
            stravaId = stravaId,
            name = name,
            brandName = brandName,
            modelName = modelName,
            distance = distance,
            configDone = configDone,
            electric = electric,
            photoUrl = photoUrl,
            fullSuspension = fullSuspension,
            draft = draft,
            type = BikeType.getByName(type),
            stats = stats?.toDomain()
        )
    }
}

data class BikeStatsEntity(
    @ColumnInfo("ride_count")
    val rideCount: Double? = null,
    @ColumnInfo("total_duration")
    val duration: Double = 0.0,
    @ColumnInfo("total_distance")
    val distance: Double = 0.0,
    @ColumnInfo("total_elevationGain")
    val elevationGain: Double = 0.0,
    @ColumnInfo("average_speed")
    val averageSpeed: Double? = null,
    @ColumnInfo("max_speed")
    val maxSpeed: Double? = null,
    @ColumnInfo("last_ride_date")
    val lastRideDate: String? = null,
) {
    fun toDomain(): BikeStats {
        return BikeStats(
            rideCount = rideCount,
            elevationGain = elevationGain,
            distance = distance,
            duration = duration,
            averageSpeed = averageSpeed,
            maxSpeed = maxSpeed,
            lastRideDate = lastRideDate?.toLocalDateTime()
        )
    }
}