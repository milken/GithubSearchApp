package com.example.milken.githubsearchapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Repo(
    override val id: Long,
    override val dataType: DataType = DataType.REPO,
    val name: String,
    val description: String?
) : BaseItem(), Parcelable {


}