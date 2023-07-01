package com.anxops.bkn.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.model.Bike

@Entity(tableName = "app_info")
data class AppInfo(
    @ColumnInfo(name = "last_rides_update") val lastRidesUpdate: Long = 0,
)  {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}