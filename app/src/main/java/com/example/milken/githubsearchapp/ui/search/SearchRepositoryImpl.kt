package com.example.milken.githubsearchapp.ui.search

import android.support.annotation.VisibleForTesting
import com.example.milken.githubsearchapp.data.apis.GithubSearchApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.ReposResponse
import com.example.milken.githubsearchapp.data.models.UsersResponse
import com.example.milken.githubsearchapp.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction

class SearchRepositoryImpl(
    private val githubSearchApi: GithubSearchApi,
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
) : SearchContract.Repository {

    private lateinit var requestCallback: RequestCallback<List<BaseItem>>

    @VisibleForTesting
    var currentRequestDisposable: Disposable? = null

    override fun setRequestCallback(requestCallback: RequestCallback<List<BaseItem>>) {
       this.requestCallback = requestCallback
    }

    override fun fetchDataWith(query: String) {
        disposeCurrentRequest()
        compositeDisposable.clear()

        val requestDisposable = createRequest(query)
        compositeDisposable.add(requestDisposable)
    }

    private fun createRequest(query: String): Disposable {
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
                { result -> requestCallback.requestSuccess(result) },
                { err -> requestCallback.requestError(err) })
    }



    override fun viewDestroyed() {
        compositeDisposable.dispose()
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

    private fun disposeCurrentRequest() {
        currentRequestDisposable?.dispose()
    }
}