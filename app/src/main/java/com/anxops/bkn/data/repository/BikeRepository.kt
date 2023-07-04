package com.anxops.bkn.data.repository

import android.util.Log
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull


interface BikeRepositoryFacade {

    suspend fun getBike(id: String): Bike?

    suspend fun getBikes(): List<Bike>

    suspend fun updateBike(bike: Bike)

    suspend fun deleteBike(bike: Bike)

    suspend fun createBike(bike: Bike)

    suspend fun updateSynchronizedBikes(ids: List<String>)

    fun getBikesFlow(draft: Boolean = false): Flow<List<Bike>>

    suspend fun reloadData(): Boolean
}

class BikeRepository(val api: Api, val db: AppDb, val ridesRepository: RidesRepositoryFacade) :
    BikeRepositoryFacade {

    override suspend fun reloadData(): Boolean {
        val success = when (val bikes = api.getBikes()) {
            is ApiResponse.Success -> {
                db.bikeDao().clear()
                bikes.data.let {
                    it.forEach { b ->
                        db.bikeDao().insert(b.toEntity())
                    }
                }
                true
            }

            else -> false
        }
        return success

    }

    override suspend fun getBike(id: String): Bike? {
        return db.bikeDao().getById(id)?.toDomain()
    }


    override suspend fun getBikes(): List<Bike> {
        return db.bikeDao().findAll().map(BikeEntity::toDomain)
    }

    override fun getBikesFlow(draft: Boolean): Flow<List<Bike>> {
        return db.bikeDao().flow().mapNotNull { list ->

            if (draft) {
                list.map {
                    it.toDomain()
                }
            } else {
                list.filter {
                    !it.draft
                }.map { it.toDomain() }
            }


        }
    }

    override suspend fun updateBike(bike: Bike) {
        db.bikeDao().update(bike.toEntity())
        api.updateBike(bike)
    }

    override suspend fun deleteBike(bike: Bike) {
        db.bikeDao().delete(bike.toEntity())
        api.deleteBike(bike)
    }

    override suspend fun createBike(bike: Bike) {
        db.bikeDao().insert(bike.toEntity())
        api.createBike(bike)
    }

    override suspend fun updateSynchronizedBikes(ids: List<String>) {
        println("updateSynchronizedBikes: " + ids.joinToString(", "))
        db.bikeDao().syncBikes(ids)
        when (val result = api.syncBikes(ids)) {
            is ApiResponse.Success -> {
                Log.d("updateSynchronizedBikes", "Result OK: ${result.message}")
            }

            else -> {
                Log.d("updateSynchronizedBikes", "Result FAIL: ${result.message}")
            }
        }
    }

}