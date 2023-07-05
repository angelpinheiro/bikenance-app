package com.anxops.bkn.data.database.dao

import androidx.room.*
import com.anxops.bkn.data.database.entities.ComponentEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface BikeComponentDao {

    @Query("SELECT * FROM component WHERE _id = :id")
    suspend fun getById(id: String): ComponentEntity?

    @Query("SELECT * FROM component")
    fun flow(): Flow<List<ComponentEntity>>

    @Query("SELECT * FROM component WHERE bike_id = :bikeId")
    fun bikeFlow(bikeId: String): Flow<List<ComponentEntity>>

    @Insert
    suspend fun insert(component: ComponentEntity)

    @Update
    suspend fun update(component: ComponentEntity)

    @Insert
    fun insertAll(vararg components: ComponentEntity)

    @Query("DELETE FROM component")
    suspend fun clear()


}