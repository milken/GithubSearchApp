package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.search.SearchActivity
import com.example.milken.githubsearchapp.search.SearchContract
import com.example.milken.githubsearchapp.search.SearchPresenterImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SearchModule {

    @Provides
    @ActivityScope
    fun getGithubSearchApi(retrofit: Retrofit): GithubSearchApi {
        return retrofit.create(GithubSearchApi::class.java)
    }

    @Provides
    @ActivityScope
    fun getSearchPresenter(githubSearchApi: GithubSearchApi): SearchContract.Presenter {
        return SearchPresenterImpl(githubSearchApi)
    }
}