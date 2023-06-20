package com.anxops.bkn.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaAthlete(
    @SerialName("id") var id: String? = null,
    @SerialName("username") var username: String? = null,
    @SerialName("resource_state") var resourceState: Int? = null,
    @SerialName("firstname") var firstname: String? = null,
    @SerialName("lastname") var lastname: String? = null,
    @SerialName("city") var city: String? = null,
    @SerialName("state") var state: String? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("sex") var sex: String? = null,
    @SerialName("premium") var premium: Boolean? = null,
    @SerialName("created_at") var createdAt: String? = null,
    @SerialName("updated_at") var updatedAt: String? = null,
    @SerialName("badge_type_id") var badgeTypeId: Int? = null,
    @SerialName("profile_medium") var profileMedium: String? = null,
    @SerialName("profile") var profile: String? = null,
    @SerialName("friend") var friend: String? = null,
    @SerialName("follower") var follower: String? = null,
    @SerialName("follower_count") var followerCount: Int? = null,
    @SerialName("friend_count") var friendCount: Int? = null,
    @SerialName("mutual_friend_count") var mutualFriendCount: Int? = null,
    @SerialName("athlete_type") var athleteType: Int? = null,
    @SerialName("date_preference") var datePreference: String? = null,
    @SerialName("measurement_preference") var measurementPreference: String? = null,
    @SerialName("ftp") var ftp: String? = null,
    @SerialName("weight") var weight: Int? = null,
    @SerialName("bikes") var bikeRefs: List<StravaBikeRef>? = null,
    @SerialName("detailedGear") var detailedGear: List<StravaDetailedGear>? = null,
)

@Serializable
data class StravaBikeRef(
    @SerialName("id") var id: String,
    @SerialName("primary") var primary: Boolean? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("resource_state") var resourceState: Int? = null,
    @SerialName("distance") var distance: Int? = null
)

@Serializable
data class StravaDetailedGear(
    @SerialName("id") var id: String? = null,
    @SerialName("primary") var primary: Boolean? = null,
    @SerialName("resource_state") var resourceState: Int? = null,
    @SerialName("distance") var distance: Int? = null,
    @SerialName("brand_name") var brandName: String? = null,
    @SerialName("model_name") var modelName: String? = null,
    @SerialName("frame_type") var frameType: Int? = null,
    @SerialName("description") var description: String? = null,
    @SerialName("name") var name: String? = null
)