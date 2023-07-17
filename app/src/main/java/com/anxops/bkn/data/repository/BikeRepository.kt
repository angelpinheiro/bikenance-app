package com.anxops.bkn.data.repository

import android.util.Log
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.Bike
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext


interface BikeRepositoryFacade {

    suspend fun getBike(id: String): Bike?

    suspend fun getBikes(): List<Bike>

    suspend fun updateBike(bike: Bike)

    suspend fun setupBike(bike: Bike)

    suspend fun deleteBike(bike: Bike)

    suspend fun createBike(bike: Bike)

    suspend fun updateSynchronizedBikes(ids: Map<String, Boolean>)

    fun getBikesFlow(draft: Boolean = false): Flow<List<Bike>>

    suspend fun reloadData(): Boolean
}

class BikeRepository(
    val api: Api,
    val db: AppDb,
    val ridesRepository: RidesRepositoryFacade,
    val componentRepository: ComponentRepositoryFacade,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BikeRepositoryFacade {

    override suspend fun reloadData(): Boolean = withContext(defaultDispatcher) {
        when (val bikes = api.getBikes()) {
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
    }

    override suspend fun getBike(id: String): Bike? = withContext(defaultDispatcher) {

        db.bikeDao().getById(id)?.let {
            it.toDomain().copy(
                components = db.bikeComponentDao().bike(id).map { c -> c.toDomain() }
            )
        }
    }


    override suspend fun getBikes(): List<Bike> = withContext(defaultDispatcher) {
        db.bikeDao().findAll().map(BikeEntity::toDomain)
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

    override suspend fun setupBike(bike: Bike) {
        when (val response = api.setupBike(bike)) {
            is ApiResponse.Error -> TODO()
            is ApiResponse.Success -> {
                db.bikeDao().update(response.data.toEntity())
                response.data.components?.let {
                    componentRepository.createComponents(response.data._id, it)
                }
            }
        }
    }

    override suspend fun deleteBike(bike: Bike) {
        db.bikeDao().delete(bike.toEntity())
        api.deleteBike(bike)
    }

    override suspend fun createBike(bike: Bike) {
        db.bikeDao().insert(bike.toEntity())
        api.createBike(bike)
    }

    override suspend fun updateSynchronizedBikes(ids: Map<String, Boolean>) {

        when (val result = api.syncBikes(ids)) {
            is ApiResponse.Success -> {
                db.bikeDao().syncBikes(ids.filter { it.value }.keys)
                Log.d("updateSynchronizedBikes", "Result OK: ${result.message}")
            }

            else -> {
                Log.d("updateSynchronizedBikes", "Result FAIL: ${result.message}")
            }
        }
    }

}