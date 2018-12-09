package com.example.milken.githubsearchapp.data.apis

import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.UsersResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubSearchApi {
    @GET("search/repositories")
    fun getRepoList(@Query("q") phrase: String): Observable<ReposResponse>

    @GET("search/users")
    fun getUserList(@Query("q") phrase: String): Observable<UsersResponse>
}