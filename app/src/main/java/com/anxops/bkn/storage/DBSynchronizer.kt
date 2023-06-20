package com.anxops.bkn.storage

import com.anxops.bkn.storage.room.AppDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class DBSynchronizer(
    private val db: AppDb,
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val rideRepository: RidesRepositoryFacade,
) {

    suspend fun refreshInTransaction(scope: CoroutineScope): Boolean {
        return db.database().run {
            return@run refresh(scope)
        }
    }

    private suspend fun refresh(scope: CoroutineScope): Boolean {
        return scope.run {
            awaitAll(
                async {
                    profileRepository.reloadData()
                },
                async {
                    bikeRepository.reloadData()
                },
                async {
                    rideRepository.reloadData()
                }
            ).all { success -> success }
        }
    }
}