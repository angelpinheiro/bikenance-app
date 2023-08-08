package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.model.ComponentModifier
import com.anxops.bkn.data.model.ComponentType
import com.anxops.bkn.data.model.Maintenance
import com.anxops.bkn.data.model.MaintenanceType
import com.anxops.bkn.data.model.RevisionFrequency
import com.anxops.bkn.data.model.RevisionUnit
import com.anxops.bkn.data.model.Usage
import com.anxops.bkn.util.toLocalDateTime

@Entity(
    tableName = "component",
        foreignKeys = [
            ForeignKey(
        entity = BikeEntity::class,
                parentColumns = arrayOf("_id"),
                childColumns = arrayOf("bike_id"),
                onDelete = ForeignKey.CASCADE
    )
        ]
)
data class ComponentEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "bike_id") val bikeId: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "usage_hours") val usageHours: Double = 0.0,
    @ColumnInfo(name = "usage_distance") val usageDistance: Double = 0.0,
    @ColumnInfo(name = "component_type") val type: String,
    @ColumnInfo(name = "modifier") val modifier: String? = null,
    @ColumnInfo(name = "from") val from: String?
) {
    fun toDomain(): BikeComponent {
        return BikeComponent(
            _id = _id,
            bikeId = bikeId,
            alias = description,
            type = ComponentType.getByName(type),
            modifier = modifier?.let { ComponentModifier.valueOf(it) },
            usage = Usage(usageHours, usageDistance),

            from = from?.toLocalDateTime()
        )
    }
}

data class UsageEntity(
    @ColumnInfo(name = "usage_duration") val duration: Double,
    @ColumnInfo(name = "usage_distance") val distance: Double
) {
    fun toDomain(): Usage {
        return Usage(duration, distance)
    }
}

@Entity(
    tableName = "maintenance",
        foreignKeys = [
            ForeignKey(
        entity = ComponentEntity::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("componentId"),
        onDelete = ForeignKey.CASCADE
    )
        ]
)
data class MaintenanceEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo("componentId") val componentId: String,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("defaultFrequencyUnit") val defaultFrequencyUnit: String,
    @ColumnInfo("defaultFrequencyEvery") val defaultFrequencyEvery: Int,
    @ColumnInfo("description") val description: String,
    @ColumnInfo("componentType") val componentType: String,
    @ColumnInfo("lastDate") var lastMaintenanceDate: String? = null,
    @Embedded var usageSinceLast: UsageEntity
) {

    fun toDomain(): Maintenance {
        return Maintenance(
            _id = _id,
            componentId = componentId,
            type = MaintenanceType.getByName(type),
//            status = status,
            componentType = ComponentType.getByName(componentType),
            defaultFrequency = RevisionFrequency(
                every = defaultFrequencyEvery,
                unit = RevisionUnit.valueOf(defaultFrequencyUnit)
            ),
            description = description,
            lastMaintenanceDate = lastMaintenanceDate?.toLocalDateTime(),
            usageSinceLast = usageSinceLast.toDomain()
        )
    }
}

/**
 * Represents the one to many relation between component and maintenances.
 * Is used by the component dao to run queries.
 */
data class ComponentWithMaintenancesEntity(
    @Embedded val component: ComponentEntity,
    @Relation(
        entity = MaintenanceEntity::class,
        parentColumn = "_id",
        entityColumn = "componentId"
    ) val maintenances: List<MaintenanceEntity>
) {
    fun toDomain(): BikeComponent {
        return component.toDomain().copy(maintenances = maintenances.map { it.toDomain() })
    }
}
