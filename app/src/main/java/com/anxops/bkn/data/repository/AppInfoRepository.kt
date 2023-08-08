package com.anxops.bkn.data.repository

import androidx.room.withTransaction
import com.anxops.bkn.data.database.AppDb
import com.anxops.bkn.data.database.entities.AppInfo
import com.anxops.bkn.data.network.Api
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface AppInfoRepositoryFacade {

    suspend fun saveLastRidesUpdate(instant: Long)

    suspend fun appInfo(): AppInfo

    fun appInfoFlow(): Flow<AppInfo>

    suspend fun saveLastRidesRefresh(currentTimeMillis: Long)
}

class AppInfoRepository(
    val api: Api,
    val db: AppDb,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppInfoRepositoryFacade {

    private suspend fun getAppInfo(): AppInfo {
        return db.appInfoDao().getAppInfo() ?: AppInfo()
    }

    override suspend fun appInfo(): AppInfo = withContext(defaultDispatcher) {
        getAppInfo()
    }

    override fun appInfoFlow(): Flow<AppInfo> {
        return db.appInfoDao().getAppInfoFlow().map { it ?: AppInfo() }
    }

    override suspend fun saveLastRidesUpdate(instant: Long) = withContext(defaultDispatcher) {
        db.database().withTransaction {
            val info = getAppInfo()
            db.appInfoDao().insertOrUpdate(info.copy(lastRidesUpdate = System.currentTimeMillis()))
        }
    }

    override suspend fun saveLastRidesRefresh(currentTimeMillis: Long) {
        db.database().withTransaction {
            val info = getAppInfo()
            db.appInfoDao().insertOrUpdate(info.copy(lastRidesRefreshRequest = System.currentTimeMillis()))
        }
    }
}
