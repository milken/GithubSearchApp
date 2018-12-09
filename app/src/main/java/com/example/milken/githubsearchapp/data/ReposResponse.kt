package com.example.milken.githubsearchapp.data

import com.google.gson.annotations.SerializedName

data class ReposResponse(
    @SerializedName("items")
    val repoList: List<Repo>
)