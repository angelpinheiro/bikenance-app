package com.anxops.bkn.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anxops.bkn.data.database.dao.AppInfoDao
import com.anxops.bkn.data.database.dao.BikeComponentDao
import com.anxops.bkn.data.database.dao.BikeDao
import com.anxops.bkn.data.database.dao.BikeRideDao
import com.anxops.bkn.data.database.dao.ProfileDao
import com.anxops.bkn.data.database.entities.AppInfo
import com.anxops.bkn.data.database.entities.BikeEntity
import com.anxops.bkn.data.database.entities.BikeRideEntity
import com.anxops.bkn.data.database.entities.ComponentEntity
import com.anxops.bkn.data.database.entities.ProfileEntity
import dagger.hilt.android.qualifiers.ApplicationContext

@Database(
    entities = [ProfileEntity::class, BikeEntity::class, BikeRideEntity::class, AppInfo::class, ComponentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun bikeDao(): BikeDao
    abstract fun bikeRideDao(): BikeRideDao
    abstract fun bikeComponentDao(): BikeComponentDao
    abstract fun appInfoDao(): AppInfoDao
}


class AppDb(@ApplicationContext context: Context) {

    private val db = createRoomDatabase(context)

    fun profileDao() = db.profileDao()
    fun bikeDao() = db.bikeDao()
    fun bikeRideDao() = db.bikeRideDao()
    fun bikeComponentDao() = db.bikeComponentDao()
    fun appInfoDao() = db.appInfoDao()

    fun database() = db

    private fun createRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "bikenance-db"
        ).build()
    }

}