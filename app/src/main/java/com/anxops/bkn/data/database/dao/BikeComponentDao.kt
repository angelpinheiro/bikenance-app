package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.ComponentEntity
import com.anxops.bkn.data.database.entities.ComponentWithMaintenancesEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.model.BikeComponent
import kotlinx.coroutines.flow.Flow


@Dao
interface BikeComponentDao {

    @Query("SELECT * FROM component WHERE _id = :id")
    suspend fun getById(id: String): ComponentEntity?

    @Query("SELECT * FROM component")
    fun flow(): Flow<List<ComponentEntity>>

    @Query("SELECT * FROM component WHERE bike_id = :bikeId")
    fun bikeFlow(bikeId: String): Flow<List<ComponentEntity>>

    @Transaction
    @Query("SELECT * FROM component WHERE bike_id = :bikeId")
    fun bike(bikeId: String): List<ComponentWithMaintenancesEntity>

    @Insert
    suspend fun insert(component: ComponentEntity)

    @Update
    suspend fun update(component: ComponentEntity)
//
//    @Query("DELETE FROM component WHERE _id = :componentId")
//    suspend fun delete(componentId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(components: List<ComponentEntity>)


//    @Query("DELETE FROM maintenance WHERE componentId = :componentId")
//    suspend fun removeAllComponentMaintenances(componentId: String)
//
//    @Transaction
//    suspend fun removeAllForBike(bike: Bike) {
//        bike.components?.forEach{
//            removeAllComponentMaintenances(it._id)
//        }
//        removeAllBikeComponents(bike._id)
//    }
//
//    @Transaction
//    suspend fun removeComponent(c: BikeComponent) {
//        removeAllComponentMaintenances(c._id)
//        delete(c._id)
//    }


    @Query("DELETE FROM component")
    suspend fun clear()


}