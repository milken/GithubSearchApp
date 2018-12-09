package com.example.milken.githubsearchapp.data.models

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("items")
    val userList: List<User>
)