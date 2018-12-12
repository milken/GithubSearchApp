package com.example.milken.githubsearchapp.ui.details

import android.support.annotation.VisibleForTesting
import com.example.milken.githubsearchapp.data.apis.GithubUserDetailsApi
import com.example.milken.githubsearchapp.data.common.RequestCallback
import com.example.milken.githubsearchapp.data.models.BaseItem
import com.example.milken.githubsearchapp.data.models.User
import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.SchedulerProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsRepositoryImpl(
    private val githubUserDetailsApi: GithubUserDetailsApi,
    private val schedulerProvider: SchedulerProvider,
    private val errorParser: ErrorParser
) : DetailsContract.Repository {

    @VisibleForTesting
    var currentRequestDisposable: Disposable? = null

    private lateinit var requestCallback: RequestCallback<User>

    override fun setRequestCallback(requestCallback: RequestCallback<User>) {
        this.requestCallback = requestCallback
    }

    override fun fetchUserDetails(query: String) {
        currentRequestDisposable = githubUserDetailsApi.getUserDetails(query)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({
                requestCallback.requestSuccess(it)
            }, {
                requestCallback.requestError(errorParser.getMessage(it))
            })
    }

    override fun viewDestroyed() {
        currentRequestDisposable?.dispose()
    }
}