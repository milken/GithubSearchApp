package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.ui.details.DetailsContract
import com.example.milken.githubsearchapp.ui.details.DetailsPresenterImpl
import com.example.milken.githubsearchapp.ui.details.DetailsRepositoryImpl
import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.SchedulerProvider
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
    fun getDetailsPresenter(detailsRepository: DetailsContract.Repository): DetailsContract.Presenter {
        return DetailsPresenterImpl(detailsRepository)
    }

    @Provides
    @ActivityScope
    fun getDetailsRepository(
        githubUserDetailsApi: GithubUserDetailsApi,
        schedulerProvider: SchedulerProvider,
        errorParser: ErrorParser
    ) : DetailsContract.Repository {
        return DetailsRepositoryImpl(githubUserDetailsApi, schedulerProvider, errorParser)
    }
}