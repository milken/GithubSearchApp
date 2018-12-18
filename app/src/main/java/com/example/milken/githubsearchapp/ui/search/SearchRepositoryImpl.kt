package com.example.milken.githubsearchapp.ui.search

import android.support.annotation.VisibleForTesting
import android.util.Log
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.UsersResponse
import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class SearchRepositoryImpl(
    private val githubSearchApi: GithubSearchApi,
    private val schedulerProvider: SchedulerProvider,
    private val errorParser: ErrorParser
) : SearchContract.Repository {

    @VisibleForTesting
    var currentRequestDisposable: Disposable? = null

    override val responseSubject: PublishSubject<ViewState<List<BaseItem>>> = PublishSubject.create()

    override fun fetchDataWith(query: String) {
        Log.d("SearchRepositoryImpl", "fetchDataWith: $query")
        currentRequestDisposable?.dispose()

        currentRequestDisposable = createRequest(query)
    }

    private fun createRequest(query: String): Disposable {
        responseSubject.onNext(ViewState.loading(true))
        return Observable
            .zip(
                getUserListRequest(query),
                getRepoListRequest(query),
                BiFunction<UsersResponse, ReposResponse, List<BaseItem>> { (userList), (repoList) ->
                    val resultList = arrayListOf<BaseItem>()
                    resultList.addAll(userList)
                    resultList.addAll(repoList)
                    resultList
                })
            .observeOn(schedulerProvider.computation())
            .flatMapIterable { t1 -> t1 }
            .sorted { t1, t2 -> (t1.id - t2.id).toInt() }
            .toList()
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { result ->
                    responseSubject.onNext(ViewState.success(result))
                    responseSubject.onNext(ViewState.loading(false))
                },
                { err ->
                    responseSubject.onNext(ViewState.failure(errorParser.getMessage(err)))
                    responseSubject.onNext(ViewState.loading(false))
                })
    }

    override fun viewDestroyed() {
        currentRequestDisposable?.dispose()
    }

    private fun getUserListRequest(query: String): Observable<UsersResponse> =
        githubSearchApi
            .getUserList(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())

    private fun getRepoListRequest(query: String): Observable<ReposResponse> =
        githubSearchApi
            .getRepoList(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.io())

}