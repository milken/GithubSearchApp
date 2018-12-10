package com.example.milken.githubsearchapp.data.apis

import com.example.milken.githubsearchapp.data.models.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubUserDetailsApi {
    @GET("search/users/{login}")
    fun getUserDetails(@Path("login") login: String): Observable<User>
}