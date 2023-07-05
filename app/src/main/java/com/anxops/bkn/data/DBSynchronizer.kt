package com.anxops.bkn.data

import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.repository.BikeRepositoryFacade
import com.anxops.bkn.data.repository.ProfileRepositoryFacade
import com.anxops.bkn.data.repository.RidesRepositoryFacade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class DBSynchronizer(
    private val db: AppDb,
    private val profileRepository: ProfileRepositoryFacade,
    private val bikeRepository: BikeRepositoryFacade,
    private val rideRepository: RidesRepositoryFacade,
) {

//    suspend fun refreshInTransaction(scope: CoroutineScope): Boolean {
//        return db.database().run {
//            return@run refresh(scope)
//        }
//    }

    suspend fun refreshProfileAndBikesInTransaction(scope: CoroutineScope): Boolean {
        return db.database().run {
            return@run refreshProfileAndBikes(scope)
        }
    }

//    private suspend fun refresh(scope: CoroutineScope): Boolean {
//        return scope.run {
//            awaitAll(
//                async {
//                    profileRepository.reloadData()
//                },
//                async {
//                    bikeRepository.reloadData()
//                },
//                async {
//                    rideRepository.reloadData()
//                }
//            ).all { success -> success }
//        }
//    }

    private suspend fun refreshProfileAndBikes(scope: CoroutineScope): Boolean {
        return scope.run {
            awaitAll(
                async {
                    profileRepository.reloadData()
                },
//                async {
////                    bikeRepository.reloadData()
//                }
            ).all { success -> success }
        }
    }
}