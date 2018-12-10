package com.example.milken.githubsearchapp.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReposResponse(
    @field:Json(name = "items")
    val repoList: List<Repo>
)