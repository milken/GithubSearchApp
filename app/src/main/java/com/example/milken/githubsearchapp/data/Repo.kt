package com.example.milken.githubsearchapp.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    @SerializedName("id") override val id: Long,
    override val type: DataType = DataType.REPO,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String
) : BaseItem(), Parcelable