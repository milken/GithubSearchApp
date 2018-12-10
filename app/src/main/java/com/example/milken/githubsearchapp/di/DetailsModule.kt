package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.ui.details.DetailsContract
import com.example.milken.githubsearchapp.ui.details.DetailsPresenterImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class DetailsModule {

    @Provides
    @ActivityScope
    fun getGithubDetailsApi(retrofit: Retrofit): GithubUserDetailsApi {
        return retrofit.create(GithubUserDetailsApi::class.java)
    }

    @Provides
    @ActivityScope
    fun getDetailsPresenter(githubUserDetailsApi: GithubUserDetailsApi): DetailsContract.Presenter {
        return DetailsPresenterImpl(githubUserDetailsApi)
    }
}