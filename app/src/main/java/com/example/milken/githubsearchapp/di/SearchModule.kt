package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.ui.search.SearchContract
import com.example.milken.githubsearchapp.ui.search.SearchPresenterImpl
import com.example.milken.githubsearchapp.ui.search.SearchRepositoryImpl
import com.example.milken.githubsearchapp.utils.RxUtil
import com.example.milken.githubsearchapp.utils.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
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
    fun getSearchRepository(
        githubSearchApi: GithubSearchApi,
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable
    ): SearchContract.Repository {
        return SearchRepositoryImpl(
            githubSearchApi, schedulerProvider, compositeDisposable
        )
    }

    @Provides
    @ActivityScope
    fun getSearchPresenter(
        searchRepository: SearchContract.Repository,
        rxUtil: RxUtil,
        compositeDisposable: CompositeDisposable
    ): SearchContract.Presenter {
        return SearchPresenterImpl(searchRepository, rxUtil, compositeDisposable)
    }
}