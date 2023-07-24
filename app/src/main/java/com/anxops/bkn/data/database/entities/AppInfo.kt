package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class AppInfo(
    @ColumnInfo(name = "last_rides_update") val lastRidesUpdate: Long = 0,
    @ColumnInfo(name = "last_rides_refresh") val lastRidesRefreshRequest: Long = 0,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}