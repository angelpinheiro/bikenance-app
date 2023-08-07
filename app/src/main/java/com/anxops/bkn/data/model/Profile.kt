package com.anxops.bkn.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val _id: String,
    @SerialName("user_id") var userId: String,
    @SerialName("username") var username: String? = null,
    @SerialName("firstname") var firstname: String? = null,
    @SerialName("lastname") var lastname: String? = null,
    @SerialName("profile_photo_url") var profilePhotoUrl: String? = null,
    @SerialName("sex") var sex: String? = null,
    @SerialName("weight") var weight: Int? = null,
    @SerialName("created_at") var createdAt: String? = null,
    @SerialName("stats") var stats: AthleteStats? = null,
    @SerialName("sync") val sync: Boolean = false
) {

    fun isNew(): Boolean {
        return createdAt == null
    }

    companion object {
        val Empty: Profile = Profile(_id = "", userId = "", sync = true)
    }
}
