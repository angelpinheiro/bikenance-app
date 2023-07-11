package com.anxops.bkn.data.repository

import android.util.Log
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.toEntity
import com.anxops.bkn.data.model.BikeComponent
import com.anxops.bkn.data.network.Api
import com.anxops.bkn.data.network.ApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface ComponentRepositoryFacade {

    suspend fun getComponent(id: String): BikeComponent?

    suspend fun getBikeComponentsFlow(bikeId: String): Flow<List<BikeComponent>>

    suspend fun createComponent(component: BikeComponent)

    suspend fun createComponents(bikeId: String, components: List<BikeComponent>)
    suspend fun updateComponent(component: BikeComponent)

    suspend fun reloadData(): Boolean
}

//TODO: WIP
class BikeComponentRepository(
    val api: Api, val db: AppDb, private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ComponentRepositoryFacade {
    override suspend fun getComponent(id: String): BikeComponent? = withContext(defaultDispatcher) {
        db.bikeComponentDao().getById(id)?.toDomain()
    }

    override suspend fun getBikeComponentsFlow(bikeId: String): Flow<List<BikeComponent>> =
        withContext(defaultDispatcher) {

            db.bikeComponentDao().bikeFlow(bikeId).map { items ->
                Log.d("createComponents", " getBikeComponentsFlow(bikeId: $bikeId) -> ${items.size} items")
                items.map { it.toDomain() }
            }
        }


    override suspend fun createComponent(component: BikeComponent) =
        withContext(defaultDispatcher) {
            //TODO: save remote first
            db.bikeComponentDao().insert(component.toEntity())
        }

    override suspend fun createComponents(bikeId: String, components: List<BikeComponent>) =
        withContext(defaultDispatcher) {
            // Save to remote
            when (val result = api.addBikeComponents(bikeId, components)) {
                is ApiResponse.Success -> {
                    // save to local db
                    Log.d("createComponents", "Created ${components.size} comp.")
                    result.data.forEach { component ->
                        Log.d("createComponents", "Inserting ${component.alias} in db")
                        db.bikeComponentDao().insert(component.toEntity())
                    }
                }

                else -> {
                    TODO("HANDLE createComponents ERROR")
                }
            }


        }

    override suspend fun updateComponent(component: BikeComponent) {
        TODO("Not yet implemented")
    }

    override suspend fun reloadData(): Boolean = withContext(defaultDispatcher) {
        TODO("TBD")
    }


}