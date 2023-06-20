package com.anxops.bkn.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.model.Bike

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
            photoUrl = photoUrl,
            draft = draft
        )
    }
}