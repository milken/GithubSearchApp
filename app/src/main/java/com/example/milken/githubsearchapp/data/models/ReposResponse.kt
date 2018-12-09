package com.example.milken.githubsearchapp.data.models

import com.google.gson.annotations.SerializedName

data class ReposResponse(
    @SerializedName("items")
    val repoList: List<Repo>
)