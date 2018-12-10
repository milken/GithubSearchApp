package com.example.milken.githubsearchapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    override val id: Long,
    override val dataType: DataType = DataType.USER,
    val login: String,
    @field:Json(name = "avatar_url") val avatarUrl: String,
    @field:Json(name = "url") val detailsUrl: String,
    @field:Json(name = "followers") val followersCount: Int? = null
) : BaseItem(), Parcelable {


    val hasDetails: Boolean
        get() = followersCount != null

}