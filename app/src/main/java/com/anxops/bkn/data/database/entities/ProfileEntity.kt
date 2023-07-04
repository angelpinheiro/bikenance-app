package com.anxops.bkn.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anxops.bkn.data.model.Profile

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "user_name") val username: String?,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "profile_photo") val profilePhoto: String?,
    @ColumnInfo(name = "createdAt") val createdAt: String?,
) {
    fun toDomain(): Profile {
        return Profile(
            _id = _id ?: "",
            userId = userId,
            username = username,
            firstname = firstName,
            lastname = lastName,
            profilePhotoUrl = profilePhoto,
            sex = null,
            weight = null,
            createdAt = createdAt
        )
    }
}