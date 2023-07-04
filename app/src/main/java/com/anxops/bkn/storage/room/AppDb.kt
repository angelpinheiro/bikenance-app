package com.anxops.bkn.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anxops.bkn.storage.room.dao.AppInfoDao
import com.anxops.bkn.storage.room.dao.BikeDao
import com.anxops.bkn.storage.room.dao.BikeRideDao
import com.anxops.bkn.storage.room.dao.ProfileDao
import com.anxops.bkn.storage.room.entities.AppInfo
import com.anxops.bkn.storage.room.entities.BikeEntity
import com.anxops.bkn.storage.room.entities.BikeRideEntity
import com.anxops.bkn.storage.room.entities.ProfileEntity
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(entities = [ProfileEntity::class, BikeEntity::class, BikeRideEntity::class, AppInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun bikeDao(): BikeDao
    abstract fun bikeRideDao(): BikeRideDao
    abstract fun appInfoDao(): AppInfoDao
}


class AppDb(@ApplicationContext context: Context) {

    private val db = createRoomDatabase(context)

    fun profileDao() = db.profileDao()
    fun bikeDao() = db.bikeDao()
    fun bikeRideDao() = db.bikeRideDao()
    fun appInfoDao() = db.appInfoDao()

    fun database() = db

    private fun createRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "bikenance-db"
        ).build()
    }

}