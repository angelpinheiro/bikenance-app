package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentInfo
import com.anxops.bkn.data.model.Usage

@Entity(tableName = "component")
data class ComponentEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "bike_id") val bikeId: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "usage_hours") val usageHours: Double = 0.0,
    @ColumnInfo(name = "usage_distance") val usageDistance: Double = 0.0,
    @ColumnInfo(name = "component_type") val componentType: String,
    @ColumnInfo(name = "component_type_name") val componentTypeName: String,
) {
    fun toDomain(): BikeComponent {
        return BikeComponent(
            _id = _id,
            bikeId = bikeId,
            alias = description,
            info = ComponentInfo(componentType, componentTypeName),
            usage = Usage(usageHours, usageDistance)
        )
    }
}