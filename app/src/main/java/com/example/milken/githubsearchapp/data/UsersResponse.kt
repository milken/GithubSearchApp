package com.example.milken.githubsearchapp.data

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("items")
    val userList: List<User>
)