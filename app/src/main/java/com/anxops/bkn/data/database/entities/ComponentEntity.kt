package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentTypes
import com.anxops.bkn.data.model.Usage
import com.anxops.bkn.util.toLocalDateTime

@Entity(tableName = "component")
data class ComponentEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "bike_id") val bikeId: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "usage_hours") val usageHours: Double = 0.0,
    @ColumnInfo(name = "usage_distance") val usageDistance: Double = 0.0,
    @ColumnInfo(name = "component_type") val type: String,
    @ColumnInfo(name = "from") val from: String?,
) {
    fun toDomain(): BikeComponent {
        return BikeComponent(
            _id = _id,
            bikeId = bikeId,
            alias = description,
            type = ComponentTypes.valueOf(type),
            usage = Usage(usageHours, usageDistance),
            from = from?.toLocalDateTime()
        )
    }
}