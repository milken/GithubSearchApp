package com.example.milken.githubsearchapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("id") override val id: Long,
    override val type: DataType = DataType.USER,
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("url") val detailsUrl: String,
    @SerializedName("followers") val followersCount: Int? = null
) : BaseItem(), Parcelable {

    val hasDetails: Boolean
        get() = followersCount != null
}